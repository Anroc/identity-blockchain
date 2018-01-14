package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.NestedSignedRequestDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.QueryRequestDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ApiController implements ProviderAPI {

    @Autowired
    private ECSignatureValidator ecSignatureValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private ProviderConfig config;

    @Override
    @PostMapping(ProviderAPIConstances.ABSOLUTE_CLAIM_ATH)
    public List<ClaimDTO> getClaims(@NotNull @Valid @RequestBody SignedRequest<BasicEthereumDTO> claimRequest) {
        if (! ecSignatureValidator.isGetRequestValid(claimRequest)) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        User user = userService.findUserByEthID(claimRequest.getEthID()).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND)
        );

        return user.getClaims()
                .stream()
                .map(ClaimDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping(ProviderAPIConstances.ABSOLUTE_INFO_PATH)
    public InfoDTO info() {
        return new InfoDTO(config.getBuildVersion(), config.getApiVersion(), config.getApplicationName());
    }

    @Override
    @PostMapping(ProviderAPIConstances.ABSOLUTE_PPR_PATH)
    public BasicEthereumDTO createPermissionContract(@PathVariable("ethID") String ethID, SignedRequest<PermissionContractCreationDTO> permissionContractCreationDTO) {
        // TODO: implement creation here
        return new BasicEthereumDTO("NO_ETH_ID");
    }

    @Override
    @PutMapping(ProviderAPIConstances.ABSOLUTE_PPR_PATH)
    public List<ClaimDTO> executeSignedQuery(
            @PathVariable("ethID") String ethID,
            @RequestBody @Valid @NotNull SignedRequest<NestedSignedRequestDTO<QueryRequestDTO>> signedQueryDTO) {

        if (! ecSignatureValidator.isGetRequestValid(signedQueryDTO)) {
            throw new ServiceException("Senders signature was invalid!", HttpStatus.FORBIDDEN);
        }
        if (! ecSignatureValidator.isGetRequestValid(signedQueryDTO.getPayload().getSignedRequest())) {
            throw new ServiceException("Users signature was invalid!", HttpStatus.FORBIDDEN);
        }

        return new ArrayList<>();
    }
}
