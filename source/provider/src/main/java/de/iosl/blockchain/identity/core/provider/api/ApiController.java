package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.shared.api.ClientAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SignedClaimRequestDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ApiController extends AbstractAuthenticator implements ClientAPI, ProviderAPI {

    @Autowired
    private ECSignatureValidator ecSignatureValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private ProviderConfig config;
    @Autowired
    private ApiService apiService;

    @Override
    @PostMapping(ProviderAPIConstances.ABSOLUTE_CLAIM_ATH)
    public List<ClaimDTO> getClaims(@NotNull @Valid @RequestBody SignedRequest<BasicEthereumDTO> claimRequest) {
        if (! ecSignatureValidator.isRequestValid(claimRequest)) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        User user = getUser(claimRequest.getEthID());

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
    public BasicEthereumDTO createPermissionContract(
            @PathVariable("ethID") @NotBlank String ethID,
            @RequestBody @Valid @NotNull  SignedRequest<PermissionContractCreationDTO> permissionContractCreationDTO) {
        checkAuthentication();
        if (! ecSignatureValidator.isRequestValid(permissionContractCreationDTO)) {
            throw new ServiceException("Sender signature was invalid", HttpStatus.FORBIDDEN);
        }

        User user = getUser(ethID);

        String permissionContract = apiService.createPermissionContract(
                permissionContractCreationDTO.getEthID(),
                user,
                permissionContractCreationDTO.getPayload().getRequiredClaims(),
                permissionContractCreationDTO.getPayload().getOptionalClaims());

        return new BasicEthereumDTO(permissionContract);

    }

    @Override
    @PutMapping(ProviderAPIConstances.ABSOLUTE_PPR_PATH)
    public List<ClaimDTO> retrieveClaimsByPPR(
            @PathVariable("ethID") String ethID,
            @RequestBody @Valid @NotNull SignedRequest<SignedClaimRequestDTO> signedClaimsRequest) {
        checkAuthentication();
        if (! ecSignatureValidator.isRequestValid(signedClaimsRequest)) {
            throw new ServiceException("Senders signature was invalid!", HttpStatus.FORBIDDEN);
        }

        validateSingedRequestList(signedClaimsRequest.getPayload().getSingedClaims());

        List<ProviderClaim> claims = apiService.getClaimsForPermissionContract(
                signedClaimsRequest.getEthID(),
                extractApprovedClaims(signedClaimsRequest.getPayload().getSingedClaims())
        );

        return claims.stream().map(ClaimDTO::new).collect(Collectors.toList());
    }

    private List<ApprovedClaim> extractApprovedClaims(List<SignedRequest<ApprovedClaim>> list) {
        return list.stream().map(SignedRequest::getPayload).collect(Collectors.toList());
    }

    private void validateSingedRequestList(@NonNull  List<SignedRequest<ApprovedClaim>> list) {
        list.forEach(
                elem -> {
                    if ( ! ecSignatureValidator.isValid(elem, elem.getEthID())) {
                        throw new ServiceException("Users signature was invalid!", HttpStatus.FORBIDDEN);
                    }
                }
        );
    }

    private User getUser(@NonNull String ethID) {
        return userService.findUserByEthID(ethID).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND)
        );
    }
}
