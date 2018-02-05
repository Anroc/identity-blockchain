package de.iosl.blockchain.identity.core.user.permission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.ClosureContentCryptEngine;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequestPayload;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.claims.ClosureExpression;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.ds.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.message.MessageService;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.core.user.claims.ClaimService;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.permission.data.ClosureRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.user.permission.db.PermissionRequestDB;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private EBAInterface ebaInterface;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PermissionRequestDB permissionRequestDB;
    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private ClaimService claimService;
    @Autowired
    private ClosureContentCryptEngine closureContentCryptEngine;

    private final EthereumSigner ethereumSigner;
    private final ObjectMapper objectMapper;

    public PermissionService() {
        this.ethereumSigner = new EthereumSigner();
        this.objectMapper = new ObjectMapper();
    }

    public void handleNewPermissionRequest(@NonNull String ethID, @NonNull String pprAddress) {
        if(! keyChain.isActive()) {
            throw new ServiceException("User is not active yet. But received PPR Request for user: [%s] and address: [%s]",
                    HttpStatus.INTERNAL_SERVER_ERROR, ethID, pprAddress);
        }

        log.info("Creating a new PermissionRequest for provider {} for ppr address {}", ethID, pprAddress);

        // 1. Request PermissionContract from EBA.
        PermissionContractContent permissionContractContent = ebaInterface.getPermissionContractContent(keyChain.getAccount(), pprAddress);

        // 2. Extract Closure Content
        Set<ClosureRequest> closureRequests = extractClosureRequests(permissionContractContent.getClosureContent());

        PermissionRequest permissionRequest = new PermissionRequest(
                UUID.randomUUID().toString(),
                permissionContractContent.getRequesterAddress(),
                ethID,
                pprAddress,
                convertToDataModel(permissionContractContent.getRequiredClaims()),
                convertToDataModel(permissionContractContent.getOptionalClaims()),
                closureRequests
        );

        permissionRequest = insertPermissionRequest(permissionRequest);

        log.info("Creating new message.");

        // 3. create message for frontend
        messageService.createMessage(MessageType.PERMISSION_REQUEST, permissionRequest.getId());
    }

    private Set<ClosureRequest> extractClosureRequests(ClosureContent closureContent) {
        Set<ClosureContractRequest> closureContractRequests =
                closureContentCryptEngine.decrypt(closureContent, keyChain.getRsaKeyPair().getPrivate());

        log.info("Requesting current user claims.");
        List<UserClaim> userClaims = claimService.getClaims();

        return closureContractRequests.stream().map(
                ccr -> evaluateClosureExpression(ccr, userClaims)
        ).collect(Collectors.toSet());
    }

    private ClosureRequest evaluateClosureExpression(
            ClosureContractRequest closureContractRequest,
            List<UserClaim> userClaims) {

        UserClaim userClaim = userClaims.stream()
                .filter(uc -> uc.getId().equals(closureContractRequest.getClosureContractRequestPayload().getClaimID()))
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException("Could not find claim with id [%s]. Need update.", HttpStatus.UNPROCESSABLE_ENTITY, closureContractRequest.getClosureContractRequestPayload().getClaimID())
                );

        ClosureExpression<?> closureExpression = new ClosureExpression<>(
                userClaim.getClaimValue(),
                closureContractRequest.getClosureContractRequestPayload().getClaimOperation(),
                closureContractRequest.getClosureContractRequestPayload().getStaticValue().getUnifiedValue()
        );

        String description = closureExpression.describe(userClaim.getId());
        log.info("Evaluating: {}", description);
        return new ClosureRequest(
                closureContractRequest,
                description,
                closureExpression.evaluate()
        );
    }

    private Map<String, Boolean> convertToDataModel(@NonNull Map<String, String> map) {
        return map.keySet().stream().collect(Collectors.toMap(s -> s, s -> false));
    }

    public PermissionRequest updatePermissionRequest(@NonNull PermissionRequest permissionRequest) {
        log.info("Start updating permission Request...");
        permissionRequest = permissionRequestDB.update(permissionRequest);

        updatePermissionContract(permissionRequest);
        createHeartBeat(permissionRequest);

        log.info("Finished updating permission Request...");
        return permissionRequest;
    }

    private void createHeartBeat(PermissionRequest permissionRequest) {
        log.info("Creating beat for permission request");
        heartBeatService.createEthIdBeat(permissionRequest.getRequestingProvider(), EventType.PPR_UPDATE, permissionRequest.getPermissionContractAddress());
    }

    private void updatePermissionContract(@NonNull PermissionRequest permissionRequest) {
        log.info("Generating signed objects...");
        Map<String, String> requiredSignedClaims;
        if(permissionRequest.getRequiredClaims() != null) {
            requiredSignedClaims = generateSignatures(permissionRequest.getRequiredClaims(), permissionRequest.getRequestingProvider());
        } else {
            requiredSignedClaims = new HashMap<>();
        }

        Map<String, String> optionalSignedClaims;
        if(permissionRequest.getOptionalClaims() != null) {
            optionalSignedClaims = generateSignatures(permissionRequest.getOptionalClaims(), permissionRequest.getRequestingProvider());
        } else {
            optionalSignedClaims = new HashMap<>();
        }

        ClosureContent closureContent = buildClosureContent(permissionRequest);

        PermissionContractContent permissionContractContent = new PermissionContractContent(
                requiredSignedClaims,
                optionalSignedClaims,
                permissionRequest.getRequestingProvider(),
                closureContent
        );

        log.info("Updating PPR in ethereum");
        ebaInterface.approvePermissionContract(keyChain.getAccount(), permissionRequest.getPermissionContractAddress(), permissionContractContent);
    }

    private ClosureContent buildClosureContent(PermissionRequest permissionRequest) {
        if(permissionRequest.getClosureRequests() == null || permissionRequest.getClosureRequests().isEmpty()) {
            return null;
        }

        RegistryEntryDTO registryEntryDTO = heartBeatService.discover(permissionRequest.getRequestingProvider())
                .orElseThrow(
                        () -> new ServiceException("Could not retrieve/validate registry entry from Discovery Service.", HttpStatus.UNPROCESSABLE_ENTITY)
                );

        String publicKey = registryEntryDTO.getPublicKey();
        String ethID = keyChain.getAccount().getAddress();

        Set<ClosureContractRequest> closureContractRequests = permissionRequest.getClosureRequests().stream()
                .filter(closureRequest -> closureRequest.isApproved())
                .map(closureRequest -> {
                    ClosureContractRequestPayload payload = closureRequest.toClosureContentRequestPayload(ethID);
                    return new ClosureContractRequest(
                            payload,
                            ECSignature.fromSignatureData(
                                    ethereumSigner.sign(payload, keyChain.getAccount().getECKeyPair())
                            )
                    );
                })
                .collect(Collectors.toSet());

        return closureContentCryptEngine.encrypt(publicKey, closureContractRequests);
    }

    private Map<String, String> generateSignatures(@NonNull Map<String, Boolean> requestedClaims, @NonNull String providerEthId) {
        return requestedClaims.keySet()
                .stream()
                .filter(requestedClaims::get)
                .collect(Collectors.toMap(s -> s, claimId -> doGenerateSignatureForKey(claimId, providerEthId)));
    }

    private String doGenerateSignatureForKey(String key, String providerEthId) {
        ApprovedClaim approvedClaim = new ApprovedClaim(keyChain.getAccount().getAddress(), key, providerEthId);
        SignedRequest<ApprovedClaim> signedRequest = new SignedRequest<>(
                approvedClaim,
                ECSignature.fromSignatureData(ethereumSigner.sign(approvedClaim, keyChain.getAccount().getECKeyPair()))
        );

        try {
            String json = objectMapper.writeValueAsString(signedRequest);
            return new String(Base64.encode(json.getBytes()));
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean verifyPermissionClaims(Map<String, Boolean> oldPermissionClaims, Map<String, Boolean> newPermissionClaims) {
        return oldPermissionClaims.keySet().containsAll(newPermissionClaims.keySet()) && newPermissionClaims.keySet().containsAll(oldPermissionClaims.keySet());
    }

    public PermissionRequest insertPermissionRequest(@NonNull PermissionRequest permissionRequest) {
        return permissionRequestDB.insert(permissionRequest);
    }

    public Optional<PermissionRequest> findPermissionRequest(@NonNull String id) {
        return permissionRequestDB.findEntity(id);
    }
}
