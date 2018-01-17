package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public String createPermissionContract(
            @NonNull String requestingProvider,
            @NonNull User user,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims) {

        // TODO: remove if interface gets adapted
        Set<String> claims = new HashSet<>(requiredClaims);
        claims.addAll(optionalClaims);

        // TODO: adapt interface to add requesting Provider
        String ppr = ebaInterface.createPermissionContract(keyChain.getAccount(), user.getEthId(), claims);
        if(ppr == null) {
            throw new ServiceException("PPR address was null!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ppr;
    }

    public List<ProviderClaim> getClaimsForPermissionContract(
            @NonNull String requestingEthID,
            @NonNull List<ApprovedClaim> requiredClaims,
            @NonNull List<ApprovedClaim> optionalClaims) {

        List<ApprovedClaim> requestedClaims = new ArrayList<>(requiredClaims);
        requestedClaims.addAll(optionalClaims);

        requestedClaims = filterForRequestingEthID(requestingEthID, requestedClaims);
        List<User> users = requestedUsers(requestedClaims);

        if(users.size() > 1) {
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
                .filter(requestedClaimIds::contains)
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
                .map(userService::findUser)
                .filter(
                        userOptional -> {
                            if(userOptional.isPresent()) {
                                return true;
                            } else {
                                log.warn("Could not find some users of [{}]", claims);
                                return false;
                            }
                        })
                .map(Optional::get)
                .collect(Collectors.toList());

    }
}
