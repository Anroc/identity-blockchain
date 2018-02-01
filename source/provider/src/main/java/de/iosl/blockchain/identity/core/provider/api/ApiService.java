package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApiService {

    @Autowired
    private EBAInterface ebaInterface;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private UserService userService;
    @Autowired
    private HeartBeatService heartBeatService;

    public String createPermissionContract(
            @NonNull String requestingProvider,
            @NonNull User user,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims) {

        if(user.getEthId() == null) {
            throw new ServiceException("User [%s] is not yet known to the system.", HttpStatus.UNPROCESSABLE_ENTITY, user.getId());
        }

        PermissionContractContent permissionContractContent = new PermissionContractContent(
                requiredClaims,
                optionalClaims,
                requestingProvider,
                null // TODO @Marvin: Implement Closure requests -- see Issue #91
        );

        String ppr = ebaInterface.deployPermissionContract(
                keyChain.getAccount(),
                user.getEthId(),
                permissionContractContent
        );

        if(ppr == null) {
            throw new ServiceException("PPR address was null!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        heartBeatService.createEthIdBeat(user.getEthId(), EventType.NEW_PPR, ppr);

        return ppr;
    }

    public List<ProviderClaim> getClaimsForPermissionContract(
            @NonNull String requestingEthID,
            @NonNull List<ApprovedClaim> requestedClaims) {

        requestedClaims = filterForRequestingEthID(requestingEthID, requestedClaims);
        List<User> users = requestedUsers(requestedClaims);

        if (users.size() > 1) {
            throw new ServiceException("It is not supported to request claims of more then 1 user. Requested was [{}]", HttpStatus.UNPROCESSABLE_ENTITY, users);
        } else if (requestedClaims.isEmpty()) {
            throw new ServiceException("This claim request is not issued for the given provider!", HttpStatus.FORBIDDEN);
        } else if (users.isEmpty()) {
            throw new ServiceException("No user could be found from the given PPR.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = users.get(0);

        List<String> requestedClaimIds = requestedClaims.stream()
                .map(ApprovedClaim::getClaimId)
                .collect(Collectors.toList());

        return user.getClaims().stream()
                .filter(claim -> requestedClaimIds.contains(claim.getId()))
                .collect(Collectors.toList());

    }

    private List<ApprovedClaim> filterForRequestingEthID(String requestingEthID, List<ApprovedClaim> claims) {
        return claims.stream()
                .filter(approvedClaim -> approvedClaim.getProviderEthId().equals(requestingEthID))
                .collect(Collectors.toList());
    }

    private List<User> requestedUsers(List<ApprovedClaim> claims) {
        return claims.stream()
                .map(ApprovedClaim::getEthID)
                .distinct()
                .map(userService::findUserByEthID)
                .filter(
                        userOptional -> {
                            if(userOptional.isPresent()) {
                                return true;
                            } else {
                                log.warn("Could not find some users of {}", claims);
                                return false;
                            }
                        })
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    public void validateClosureExpression(@NonNull User user, @NonNull ClosureContractRequestDTO closureContractRequestDTO) {
        Optional<ProviderClaim> providerClaimOptional = user.findClaim(closureContractRequestDTO.getClaimID());

        if(! providerClaimOptional.isPresent()) {
            throw new ServiceException(
                    "Claim with id [%s] was not found for [%s].",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    closureContractRequestDTO.getClaimID(), user.getEthId());
        }

        ProviderClaim providerClaim = providerClaimOptional.get();
        ClaimType claimType = providerClaim.getClaimValue().getPayloadType();

        if(! claimType.supports(closureContractRequestDTO.getClaimOperation())) {
            throw new ServiceException(
                    "Claim with id [%s] does not support [%s]",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    closureContractRequestDTO.getClaimID(),
                    closureContractRequestDTO.getClaimOperation()
            );
        }

        if(! claimType.validateType(closureContractRequestDTO.getStaticValue())) {
            throw new ServiceException(
                    "Claim with id [%s] is not of expected type [%s].",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    closureContractRequestDTO.getClaimID(),
                    claimType
            );
        }

    }
}
