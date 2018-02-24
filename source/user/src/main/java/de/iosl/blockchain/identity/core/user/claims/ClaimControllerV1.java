package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.user.claims.claim.dto.UserClaimDTOV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/claims")
public class ClaimControllerV1 {

    @Autowired
    private ClaimController claimControllerV2;

    /**
     * @Deprecated here for legacy reasons. This endpoint does not expose the claim signature
     * @return List of unsigned claims
     */
    @Deprecated
    @GetMapping
    public List<UserClaimDTOV1> getClaimsV1() {
        return claimControllerV2.getClaims().stream().map(UserClaimDTOV1::new).collect(Collectors.toList());
    }
}
