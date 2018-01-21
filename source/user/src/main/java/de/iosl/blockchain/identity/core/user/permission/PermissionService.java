package de.iosl.blockchain.identity.core.user.permission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.message.MessageService;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
        PermissionRequest permissionRequest = new PermissionRequest(
                UUID.randomUUID().toString(),
                permissionContractContent.getRequesterAddress(),
                ethID,
                pprAddress,
                convertToDataModel(permissionContractContent.getRequiredClaims()),
                convertToDataModel(permissionContractContent.getOptionalClaims())
        );

        permissionRequest = insertPermissionRequest(permissionRequest);

        log.info("Creating new message.");

        // 2. create message for frontend
        messageService.createMessage(MessageType.PERMISSION_REQUEST, permissionRequest.getId());
    }

    private Map<String, Boolean> convertToDataModel(@NonNull Map<String, String> map) {
        return map.keySet().stream().collect(Collectors.toMap(s -> s, s -> false));
    }

    public PermissionRequest updatePermissionRequest(@NonNull PermissionRequest permissionRequest) {
        log.info("Start updating permission Request...");
        PermissionRequest permissionRequestOld = permissionRequestDB.findEntity(permissionRequest.getId())
                .orElseThrow(() -> new IllegalStateException("Could not find old object Permission object"));

        if(! verifyPermissionClaims(permissionRequestOld.getRequiredClaims(), permissionRequest.getRequiredClaims())) {
            throw new ServiceException("Updated required claim set contained more/less keys then the old one.", HttpStatus.BAD_REQUEST);
        }

        if(! verifyPermissionClaims(permissionRequestOld.getOptionalClaims(), permissionRequest.getOptionalClaims())) {
            throw new ServiceException("Updated optional claim set contained more/less keys then the old one.", HttpStatus.BAD_REQUEST);
        }

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
        Map<String, String> requiredSignedClaims = generateSignatures(permissionRequest.getRequiredClaims(), permissionRequest.getRequestingProvider());
        Map<String, String> optionalSignedClaims = generateSignatures(permissionRequest.getOptionalClaims(), permissionRequest.getRequestingProvider());

        PermissionContractContent permissionContractContent = new PermissionContractContent(
                requiredSignedClaims,
                optionalSignedClaims,
                permissionRequest.getRequestingProvider()
        );

        log.info("Updating PPR in ethereum");
        ebaInterface.approvePermissionContract(keyChain.getAccount(), permissionRequest.getPermissionContractAddress(), permissionContractContent);
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
