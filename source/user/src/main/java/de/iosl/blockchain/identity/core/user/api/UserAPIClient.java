package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.api.ClientAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface UserAPIClient extends ClientAPI {

    @Override
    @Headers("Content-Type: application/json")
    @RequestLine("POST " + ProviderAPIConstances.ABSOLUTE_CLAIM_ATH)
    List<ClaimDTO> getClaims(SignedRequest<BasicEthereumDTO> claimRequest);

    @Override
    @RequestLine("GET " + ProviderAPIConstances.ABSOLUTE_INFO_PATH)
    InfoDTO info();
}
