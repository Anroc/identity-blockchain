package de.iosl.blockchain.identity.core.provider.permission;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.core.provider.api.client.APIProviderService;
import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.PermissionGrand;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.message.MessageService;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionRequestService {

    @Autowired
    private APIProviderService apiProviderService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EBAInterface ebaInterface;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private HeartBeatService heartBeatService;

    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    private final ECSignatureValidator ecSignatureValidator = new ECSignatureValidator();

    public void requestPermission(@NonNull PermissionRequest permissionRequest) {
        String pprAddress = apiProviderService.requestUserClaims(
                permissionRequest.getUrl(),
                permissionRequest.getEthID(),
                permissionRequest.getRequiredClaims(),
                permissionRequest.getOptionalClaims());

        log.info("Retrieved PPR address at {}", pprAddress);

        Optional<User> userOptional = userService.findUserByEthID(permissionRequest.getEthID());

        if(! userOptional.isPresent()) {
            createNewUser(pprAddress, permissionRequest);
        } else {
            updateUser(userOptional.get(), pprAddress, permissionRequest);
        }

        registerPermissionContractListener(pprAddress, permissionRequest.getEthID(), permissionRequest.getUrl());
    }

    protected void registerPermissionContractListener(String pprAddress, String ethID, String url) {
        heartBeatService.subscribe(
                (event, eventType) -> {
                    switch (eventType) {
                        case PPR_UPDATE:
                            log.info("Received new PPR update event for ethID {}", event.getSubject());
                            if(event.getSubjectType() != SubjectType.ETHEREUM_ADDRESS) {
                                throw new IllegalStateException("Event was a PPR update but not of type Etherem Address");
                            }

                            if(event.getSubject().equals(pprAddress)) {
                                permissionContractUpdateHandler(event.getSubject(), ethID, url);
                            }
                            break;
                    }
                }
        );
    }

    protected void permissionContractUpdateHandler(String pprAddress, String ethID, String url) {
        PermissionContractContent contractContent =
                ebaInterface.getPermissionContractContent(keyChain.getAccount(), pprAddress);

        if (! keyChain.isActive()) {
            // TODO: better handle error state. e.g. save state in database
            log.error("PPR received but provider is offline. Failing gracefully.");
            return;
        }

        Map<String, String> claimResult = new HashMap<>(contractContent.getRequiredClaims());
        claimResult.putAll(contractContent.getOptionalClaims());

        log.info("Received claims via PPR: requried {} and optional {} for user {}", claimResult, ethID);

        List<SignedRequest<ApprovedClaim>> approvedClaims = mapResultsToApprovedClaimRequest(claimResult);
        log.info("Successful extracted {} approvedClaims.", approvedClaims.size());

        validateApprovedClaims(approvedClaims);
        log.info("Successful validated {} approvedClaims for user [{}]", ethID, approvedClaims);

        List<ProviderClaim> claims = apiProviderService.requestClaimsForPPR(url, ethID, pprAddress, approvedClaims);

        log.info("Received claims from Provider: {}", claims);
        User user = updateUserClaims(ethID, claims);
        updateUserPermissionGrants(user, pprAddress, claims);

        messageService.createMessage(MessageType.NEW_CLAIMS, user.getId(), null);
    }

    @SuppressWarnings("unchecked")
    private List<SignedRequest<ApprovedClaim>> mapResultsToApprovedClaimRequest(@NonNull Map<String, String> claimResult) {
        TypeReference<SignedRequest<ApprovedClaim>> typeReference = new TypeReference<SignedRequest<ApprovedClaim>>() {};

        return claimResult.values()
                .stream()
                .filter(Objects::nonNull)
                .filter(base64 -> ! base64.isEmpty())
                .map(Base64::decode)
                .map(json -> {
                    try {
                        return (SignedRequest<ApprovedClaim>) getObjectMapper().readValue(json, typeReference);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }).collect(Collectors.toList());
    }

    private void validateApprovedClaims(List<SignedRequest<ApprovedClaim>> approvedClaims) {
        approvedClaims.forEach(
                approvedClaimSignedRequest -> {
                    if(! getEcSignatureValidator().isRequestValid(approvedClaimSignedRequest)) {
                        throw new ServiceException("ApproveClaim request was not valid! DTO: %s", HttpStatus.INTERNAL_SERVER_ERROR, approvedClaimSignedRequest);
                    }
                }
        );
    }

    private User updateUserClaims(String ethID, List<ProviderClaim> claims) {
        User user = userService.findUserByEthID(ethID).orElseThrow(
                () -> new ServiceException("User who's claims shell be updated was not found! EthID: [%s]",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ethID));
        Date currentDate = new Date();
        claims.forEach(
                claim -> {
                    claim.setModificationDate(currentDate);
                    user.putClaim(claim);
                }
        );

        return userService.updateUser(user);
    }

    private User updateUserPermissionGrants(User user, String permissionContractAddress, List<ProviderClaim> claims) {
        PermissionGrand permissionGrand = user.findPermissionGrand(permissionContractAddress)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User [%s] does not have matching permission grands (%s).", user.getId(), permissionContractAddress))
                );

        permissionGrand.setRequiredClaimGrants(updatePermissionGrands(permissionGrand.getRequiredClaimGrants(), claims));
        permissionGrand.setOptionalClaimGrants(updatePermissionGrands(permissionGrand.getOptionalClaimGrants(), claims));

        user.putPermissionGrant(permissionGrand);

        return userService.updateUser(user);
    }

    private Map<String, Boolean> updatePermissionGrands(Map<String, Boolean> grands, List<ProviderClaim> claims) {
        claims.stream().map(ProviderClaim::getId).forEach(
                key -> {
                    if(grands.get(key) != null) {
                        grands.put(key, true);
                    }
                }
        );

        return grands;
    }

    private void updateUser(User user, String pprAddress, PermissionRequest permissionRequest) {
        PermissionGrand permissionGrand =
                PermissionGrand.init(pprAddress, permissionRequest.getRequiredClaims(), permissionRequest.getOptionalClaims());
        user.addPermissionGrant(permissionGrand);
        userService.updateUser(user);
    }

    private void createNewUser(String pprAddress, PermissionRequest permissionRequest) {
        List<PermissionGrand> permissionGrands = new ArrayList<>();
        permissionGrands.add(PermissionGrand.init(pprAddress, permissionRequest.getRequiredClaims(), permissionRequest.getOptionalClaims()));

        User user = new User(
                UUID.randomUUID().toString(),
                null,
                permissionRequest.getEthID(),
                null,
                permissionGrands,
                new HashSet<>()
        );

        userService.insertUser(user);
    }
}
