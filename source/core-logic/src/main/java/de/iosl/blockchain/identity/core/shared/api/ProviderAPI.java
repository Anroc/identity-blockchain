package de.iosl.blockchain.identity.core.shared.api;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SignedClaimRequestDTO;

import java.util.List;

public interface ProviderAPI {

    /**
     * Initialises a new PPR Contract with the given attributes.
     *
     * @param ethID the users ethID to whom the PPR shell be addressed.
     * @param permissionContractCreationDTO the DTO with the query attributes
     * @return the ID of the generated smart contract
     */
    BasicEthereumDTO createPermissionContract(String ethID, SignedRequest<PermissionContractCreationDTO> permissionContractCreationDTO);

    /**
     * Queries a provider for a given signed query.
     *
     * @param ethID the users ethID to whom the PPR shell be addressed.
     * @param singedClaimsRequest list of singed claim ids that is signed by the user.
     *                            That request is signed again by the requesting party
     * @return List of requested claims.
     */
    List<ClaimDTO> retrieveClaimsByPPR(String ethID, SignedRequest<SignedClaimRequestDTO> singedClaimsRequest);
}
