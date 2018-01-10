package de.iosl.blockchain.identity.core.user.ethereum;

import de.iosl.blockchain.identity.core.user.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.user.ethereum.dto.RegisterContractResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ethereum")
public class BlockchainController extends AbstractAuthenticator {

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping("/contract/register")
    public RegisterContractResultDTO getRegisterContractResult() {
        checkAuthentication();

        boolean approved = blockchainService.getApprovalOfRegisterContract();
        return new RegisterContractResultDTO(approved);
    }
}
