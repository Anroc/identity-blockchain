package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.shared.api.ClientAPI;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPI;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.*;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances.*;

@Slf4j
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
    @PostMapping(ABSOLUTE_CLAIM_ATH)
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
    @GetMapping(ABSOLUTE_INFO_PATH)
    public InfoDTO info() {
        return new InfoDTO(config.getBuildVersion(), config.getApiVersion(), config.getApplicationName());
    }

    @Override
    @PostMapping(ABSOLUTE_PPR_PATH)
    public BasicEthereumDTO createPermissionContract(
            @PathVariable(ETH_ID_PARAM) @NotBlank String ethID,
            @RequestBody @Valid @NotNull  SignedRequest<PermissionContractCreationDTO> permissionContractCreationDTO) {
        checkAuthentication();
        if (! ecSignatureValidator.isRequestValid(permissionContractCreationDTO)) {
            throw new ServiceException("Sender signature was invalid", HttpStatus.FORBIDDEN);
        }
        User user = getUser(ethID);

        validateClosureExpression(user, permissionContractCreationDTO.getPayload().getClosureContractRequestDTOs());

        String permissionContract = apiService.createPermissionContract(
                permissionContractCreationDTO.getEthID(),
                user,
                permissionContractCreationDTO.getPayload().getRequiredClaims(),
                permissionContractCreationDTO.getPayload().getOptionalClaims(),
                permissionContractCreationDTO.getPayload().getClosureContractRequestDTOs());

        return new BasicEthereumDTO(permissionContract);

    }

    private void validateClosureExpression(User user, Set<ClosureContractRequestDTO> closureContractRequestDTOs) {
        closureContractRequestDTOs.forEach(
                (closureContractRequestDTO) -> apiService.validateClosureExpression(user, closureContractRequestDTO)
        );
    }

    @Override
    @PutMapping(ABSOLUTE_PPR_PATH)
    public PermissionContractResponse retrieveClaimsByPPR(
            @PathVariable(ETH_ID_PARAM) String ethID,
            @RequestBody @Valid @NotNull SignedRequest<SignedClaimRequestDTO> signedClaimsRequest) {
        checkAuthentication();
        if (! ecSignatureValidator.isRequestValid(signedClaimsRequest)) {
            throw new ServiceException("Senders signature was invalid!", HttpStatus.FORBIDDEN);
        }

        User user = getUser(ethID);

        log.info("Received {} Claims retrival by PPR contract", signedClaimsRequest.getPayload().getSingedClaims().size());

        validateSingedRequestList(ethID, signedClaimsRequest.getPayload().getSingedClaims());

        List<ProviderClaim> claims = apiService.getClaimsForPermissionContract(
                signedClaimsRequest.getEthID(),
                extractApprovedClaims(signedClaimsRequest.getPayload().getSingedClaims())
        );

        log.info("Received {} Closures requests by PPR contract.", signedClaimsRequest.getPayload().getClosureContractRequests().size());

        validateSingedClosureContractSet(ethID, signedClaimsRequest.getPayload().getClosureContractRequests());

        List<SignedRequest<Closure>> closures = apiService.evaluateAndSignClosures(
                user,
                extractClosures(signedClaimsRequest.getPayload().getClosureContractRequests())
        );


        log.info("Returning {} to provider", claims);
        List<ClaimDTO> claimDTOs = claims.stream()
                .map(ClaimDTO::new)
                .map(claimDTO -> {
                    claimDTO.setSignedClosures(null);
                    return claimDTO;
                })
                .collect(Collectors.toList());
        return new PermissionContractResponse(claimDTOs, closures);
    }

    private List<Closure> extractClosures(@NonNull Set<ClosureContractRequest> closureContractRequests) {
        return closureContractRequests.stream()
                .map(ClosureContractRequest::getClosureContractRequestPayload)
                .map(Closure::init)
                .collect(Collectors.toList());
    }

    private List<ApprovedClaim> extractApprovedClaims(List<SignedRequest<ApprovedClaim>> list) {
        return list.stream().map(SignedRequest::getPayload).collect(Collectors.toList());
    }

    private void validateSingedRequestList(@NonNull String ethID, @NonNull List<SignedRequest<ApprovedClaim>> list) {
        list.forEach(
                elem -> {
                    if ( ! ecSignatureValidator.isValid(elem, elem.getEthID()) || ! elem.getEthID().equals(ethID)) {
                        throw new ServiceException("Users signature was invalid for claim request.", HttpStatus.FORBIDDEN);
                    }
                }
        );
    }

    private void validateSingedClosureContractSet(@NonNull String ethID, @NonNull Set<ClosureContractRequest> set) {
        set.forEach(
                elem -> {
                    if( ! ecSignatureValidator.isSignatureValid(
                            elem.getClosureContractRequestPayload(),
                            elem.getEcSignature(),
                            elem.getClosureContractRequestPayload().getEthID())
                            || ! elem.getClosureContractRequestPayload().getEthID().equals(ethID)) {
                        throw new ServiceException("User signature was invalid for closure request.", HttpStatus.FORBIDDEN);
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
