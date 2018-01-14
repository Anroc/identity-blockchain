package de.iosl.blockchain.identity.core.provider.account;

import de.iosl.blockchain.identity.core.shared.account.data.dto.LoginRequest;
import de.iosl.blockchain.identity.core.shared.account.data.dto.LoginResponse;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.config.ClientType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ProviderAccountService accountService;
    @Autowired
    private BlockchainIdentityConfig config;

    @PostMapping("/register")
    public LoginResponse register(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        if(config.getType() == ClientType.GOVERNMENT) {
            throw new ServiceException("Government can register a new wallet. Need to be a provider.",
                    HttpStatus.BAD_REQUEST);
        }
        String password = loginRequest.getPassword();
        return new LoginResponse(accountService.register(password));
    }
}
