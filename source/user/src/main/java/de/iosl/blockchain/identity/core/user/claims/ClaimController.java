package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @GetMapping
    public List<ClaimDTO> getClaims() {
        return claimService.getClaims().stream().map(ClaimDTO::new).collect(Collectors.toList());
    }
}
