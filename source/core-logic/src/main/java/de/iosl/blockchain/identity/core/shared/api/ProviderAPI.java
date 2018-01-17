package de.iosl.blockchain.identity.core.shared.api;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SingedClaimRequestDTO;

import java.util.List;

public interface ProviderAPI {

    /**
     * Returns all claims for the given ethereum id.
     *
     * @param claimRequest signed claim request of the requested ethereum id
     * @return list of known claims to his provider
     */
    List<ClaimDTO> getClaims(SignedRequest<BasicEthereumDTO> claimRequest);

    /**
     * Returns basic information about the provider.
     *
     * @return {@link InfoDTO} with basic information about the provider
     */
    InfoDTO info();

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
    List<ClaimDTO> retrieveClaimsByPPR(String ethID, SignedRequest<SingedClaimRequestDTO> singedClaimsRequest);

}
