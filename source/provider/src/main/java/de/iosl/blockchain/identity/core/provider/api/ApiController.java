package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    private BlockchainIdentityConfig config;

    @Override
    @PostMapping(ProviderAPIConstances.ABSOLUTE_CLAIM_ATH)
    public List<ClaimDTO> getClaims(@NotNull @Valid @RequestBody ApiRequest<String> claimRequest) {
        if (! ecSignatureValidator.isGetRequestValid(claimRequest)) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        User user = userService.findUserByEthID(claimRequest.getPayload()).orElseThrow(
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
}
