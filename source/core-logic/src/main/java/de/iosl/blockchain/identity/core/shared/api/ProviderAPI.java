package de.iosl.blockchain.identity.core.shared.api;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;

import java.util.List;

public interface ProviderAPI {

    List<ClaimDTO> getClaims(ApiRequest<String> claimRequest);

    InfoDTO info();
}
