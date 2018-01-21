package de.iosl.blockchain.identity.core.provider.api.client;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientBeanFactory;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientRegistry;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SignedClaimRequestDTO;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class APIProviderService {

    @Autowired
    private KeyChain keyChain;

    private final EthereumSigner ethereumSigner;
    private final APIClientRegistry<ProviderAPIClient> apiClientRegistry;

    @Autowired
    public APIProviderService(APIClientBeanFactory apiClientBeanFactory) {
        this.ethereumSigner = new EthereumSigner();
        this.apiClientRegistry = new APIClientRegistry<>(apiClientBeanFactory, ProviderAPIClient.class);
    }

    /**
     * Requests a given provider (via the URL) for the given set of requiredClaims and optionalClaims.
     * The request is issued for the given userEthID.
     *
     * On success the address of the permission contract is returned.
     *
     * @param url the url of the requested provider holding claims
     * @param userEthID the ethID of the user that whats to share this claims
     * @param requiredClaims a set of required claim ids
     * @param optionalClaims a set of optional claim ids
     * @return the address of the smart contract
     */
    public String requestUserClaims(
            @NonNull String url,
            @NonNull String userEthID,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims) {

        if(! keyChain.isActive()) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED);
        }

        ProviderAPIClient providerAPIClient = apiClientRegistry.getOrRegister(url);

        PermissionContractCreationDTO pcc = new PermissionContractCreationDTO(
                keyChain.getAccount().getAddress(),
                requiredClaims,
                optionalClaims
        );
        SignedRequest<PermissionContractCreationDTO> signedRequest = new SignedRequest<>(
                pcc,
                ECSignature.fromSignatureData(ethereumSigner.sign(pcc, keyChain.getAccount().getECKeyPair()))
        );

        log.info("Creating permission contract for {} at url {}", userEthID, url);
        BasicEthereumDTO basicEthereumDTO = providerAPIClient.createPermissionContract(userEthID, signedRequest);
        return basicEthereumDTO.getEthID();
    }

    /**
     * Requests the given url for new user claims approved by the given approvedClaims list.
     *
     * @param url the url of the provider
     * @param ethID the ethID of the user
     * @param pprAddress the permission contract address that backs this approval
     * @param approvedClaims the list of approved claims
     * @return list of provider claims
     */
    public List<ProviderClaim> requestClaimsForPPR(
            @NonNull String url,
            @NonNull String ethID,
            @NonNull String pprAddress,
            @NonNull List<SignedRequest<ApprovedClaim>> approvedClaims) {

        // create request
        SignedClaimRequestDTO signedClaimRequestDTO = new SignedClaimRequestDTO(
                keyChain.getAccount().getAddress(),
                pprAddress,
                approvedClaims
        );

        SignedRequest<SignedClaimRequestDTO> signedRequest = new SignedRequest<>(
                signedClaimRequestDTO,
                ECSignature.fromSignatureData(
                        ethereumSigner.sign(signedClaimRequestDTO, keyChain.getAccount().getECKeyPair()))
        );

        List<ClaimDTO> claimDTOS = apiClientRegistry.getOrRegister(url).retrieveClaimsByPPR(ethID, signedRequest);

        return claimDTOS.stream().map(ProviderClaim::new).collect(Collectors.toList());
    }
}
