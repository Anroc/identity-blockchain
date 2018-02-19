package de.iosl.blockchain.identity.core.provider.permission;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.core.provider.api.client.APIProviderService;
import de.iosl.blockchain.identity.core.provider.permission.data.ClosureRequest;
import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.PermissionGrand;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.ClosureContentCryptEngine;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractResponse;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
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
    @Autowired
    private ClosureContentCryptEngine closureContentCryptEngine;

    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    private final ECSignatureValidator ecSignatureValidator = new ECSignatureValidator();

    public void requestPermission(@NonNull PermissionRequest permissionRequest) {
        String pprAddress = apiProviderService.requestUserClaims(permissionRequest);

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

        log.info("Received claims via PPR: requried {}, optional {} and closures {} for user {}",
                contractContent.getRequiredClaims(),
                contractContent.getOptionalClaims(),
                contractContent.getClosureContent(),
                ethID);

        List<SignedRequest<ApprovedClaim>> approvedClaims = mapResultsToApprovedClaimRequest(claimResult);
        log.info("Successful extracted {} approvedClaims.", approvedClaims.size());
        validateApprovedClaims(approvedClaims);
        log.info("Successful validated {} approvedClaims for user [{}]", approvedClaims, ethID);

        List<ClosureContractRequest> closureContractRequests;
        if(contractContent.getClosureContent() != null) {
            closureContractRequests = closureContentCryptEngine.decrypt(contractContent.getClosureContent(), keyChain.getRsaKeyPair().getPrivate());
            log.info("Successful decrypted {} approved closure requests.", closureContractRequests.size());
            validateClosures(closureContractRequests);
            log.info("Successful validated {} approved closure requests {}", closureContractRequests);
        } else {
            log.info("No closures found.");
            closureContractRequests = new ArrayList<>();
        }

        PermissionContractResponse response = apiProviderService.requestClaimsForPPR(url, ethID, pprAddress, approvedClaims, closureContractRequests);

        List<ProviderClaim> claims = response.getClaims().stream().map(ProviderClaim::new).collect(Collectors.toList());
        log.info("Received claims from Provider: {}", claims);
        User user = updateUserClaims(ethID, claims);

        user = updateUserClosures(user, response.getSignedClosures());

        updateUserPermissionGrants(user, pprAddress, claims, response.getSignedClosures());
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

    private void validateClosures(List<ClosureContractRequest> closureContractRequests) {
        closureContractRequests.forEach(
                closureContractRequest -> {
                    if(! getEcSignatureValidator().isSignatureValid(
                            closureContractRequest.getClosureContractRequestPayload(),
                            closureContractRequest.getEcSignature(),
                            closureContractRequest.getClosureContractRequestPayload().getEthID())) {
                        throw new ServiceException("Closure request was not valid! Object: %s", HttpStatus.INTERNAL_SERVER_ERROR, closureContractRequest);
                    }
                }
        );
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
                    Optional<ProviderClaim> providerClaim = user.findClaim(claim.getId());
                    if(providerClaim.isPresent()) {
                        List<SignedRequest<Closure>> closures = providerClaim.get().getSignedClosures();
                        if(claim.getSignedClosures() == null) {
                            claim.setSignedClosures(closures);
                        } else {
                            claim.getSignedClosures().addAll(closures);
                        }
                    }
                    user.putClaim(claim);
                }
        );

        return userService.updateUser(user);
    }

    private User updateUserClosures(User user, List<SignedRequest<Closure>> signedClosures) {
        Set<String> newClaimIds = signedClosures.stream()
                .map(SignedRequest::getPayload)
                .map(Closure::getClaimID)
                .collect(Collectors.toSet());

        Set<String> knownClaimIds = user.getClaims().stream().map(SharedClaim::getId).collect(Collectors.toSet());
        newClaimIds.removeAll(knownClaimIds);

        initClaimForClosure(user, newClaimIds);

        for(ProviderClaim providerClaim: user.getClaims()) {
            List<SignedRequest<Closure>> closures = signedClosures.stream()
                    .filter(signedClosure -> signedClosure.getPayload().getClaimID().equals(providerClaim.getId()))
                    .collect(Collectors.toList());

            if(providerClaim.getSignedClosures() == null) {
                providerClaim.setSignedClosures(closures);
            } else {
                providerClaim.getSignedClosures().addAll(closures);
            }
        }

        return userService.updateUser(user);
    }

    private void initClaimForClosure(User user, Set<String> newClaimIds) {
        newClaimIds.stream()
                .map(claimId -> new ProviderClaim(claimId, new Date(), null, null))
                .forEach(user::putClaim);
    }

    private User updateUserPermissionGrants(User user, String permissionContractAddress, List<ProviderClaim> claims, List<SignedRequest<Closure>> signedClosures) {
        PermissionGrand permissionGrand = user.findPermissionGrand(permissionContractAddress)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User [%s] does not have matching permission grands (%s).", user.getId(), permissionContractAddress))
                );

        permissionGrand.setRequiredClaimGrants(updatePermissionGrands(permissionGrand.getRequiredClaimGrants(), claims));
        permissionGrand.setOptionalClaimGrants(updatePermissionGrands(permissionGrand.getOptionalClaimGrants(), claims));
        permissionGrand.setClosureRequests(updateClosureRequests(permissionGrand.getClosureRequests(), signedClosures));

        user.putPermissionGrant(permissionGrand);

        return userService.updateUser(user);
    }

    private List<ClosureRequest> updateClosureRequests(List<ClosureRequest> closureRequests, List<SignedRequest<Closure>> signedClosures) {
        if(signedClosures.isEmpty()) {
            return closureRequests;
        }

        for(ClosureRequest closureRequest : closureRequests) {
            if(signedClosures.stream().anyMatch(signedClosure ->
                    signedClosure.getPayload().getClaimID().equals(closureRequest.getClaimID())
                            && signedClosure.getPayload().getClaimOperation() == closureRequest.getClaimOperation()
                            && signedClosure.getPayload().getStaticValue().getUnifiedValue().equals(closureRequest.getStaticValue().getUnifiedValue()))) {
                closureRequest.setApproved(true);
            }
        }

        return closureRequests;
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
        PermissionGrand permissionGrand = PermissionGrand.init(
                pprAddress,
                permissionRequest.getRequiredClaims(),
                permissionRequest.getOptionalClaims(),
                permissionRequest.getClosuresRequests()
        );
        user.addPermissionGrant(permissionGrand);
        userService.updateUser(user);
    }

    private void createNewUser(String pprAddress, PermissionRequest permissionRequest) {
        List<PermissionGrand> permissionGrands = new ArrayList<>();
        permissionGrands.add(
                PermissionGrand.init(
                        pprAddress,
                        permissionRequest.getRequiredClaims(),
                        permissionRequest.getOptionalClaims(),
                        permissionRequest.getClosuresRequests()));

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
