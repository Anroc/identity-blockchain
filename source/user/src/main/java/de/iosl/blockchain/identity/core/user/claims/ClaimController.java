package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.user.claims.claim.dto.UserClaimDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/claims")
public class ClaimController extends AbstractAuthenticator {

    @Autowired
    private ClaimService claimService;

    @GetMapping
    public List<UserClaimDTO> getClaims() {
        checkAuthentication();

        return claimService.getClaims().stream().map(UserClaimDTO::new).collect(Collectors.toList());
    }
}
