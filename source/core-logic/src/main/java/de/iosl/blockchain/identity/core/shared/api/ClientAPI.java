package de.iosl.blockchain.identity.core.shared.api;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;

import java.util.List;

public interface ClientAPI {

    /**
     * Returns basic information about the provider.
     *
     * @return {@link InfoDTO} with basic information about the provider
     */
    InfoDTO info();

    /**
     * Returns all claims for the given ethereum id.
     *
     * @param claimRequest signed claim request of the requested ethereum id
     * @return list of known claims to his provider
     */
    List<ClaimDTO> getClaims(SignedRequest<BasicEthereumDTO> claimRequest);

}
