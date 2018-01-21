package de.iosl.blockchain.identity.core.provider.api.client;

import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SignedClaimRequestDTO;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface ProviderAPIClient extends ProviderAPI {

    @Override
    @RequestLine("POST " + ProviderAPIConstances.ABSOLUTE_PPR_PATH)
    BasicEthereumDTO createPermissionContract(@Param("ethID") String ethID, SignedRequest<PermissionContractCreationDTO> permissionContractCreationDTO);

    @Override
    @RequestLine("PUT " + ProviderAPIConstances.ABSOLUTE_PPR_PATH)
    List<ClaimDTO> retrieveClaimsByPPR(@Param("ethID") String ethID, SignedRequest<SignedClaimRequestDTO> singedClaimsRequest);
}
