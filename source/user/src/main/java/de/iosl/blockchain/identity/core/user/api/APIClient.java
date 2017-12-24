package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import feign.RequestLine;

import java.util.List;

public interface APIClient extends ProviderAPI {

    @Override
    @RequestLine("POST " + ProviderAPIConstances.ABSOLUTE_CLAIM_ATH)
    List<ClaimDTO> getClaims(ApiRequest<String> claimRequest);

    @Override
    @RequestLine("GET " + ProviderAPIConstances.ABSOLUTE_INFO_PATH)
    InfoDTO info();
}
