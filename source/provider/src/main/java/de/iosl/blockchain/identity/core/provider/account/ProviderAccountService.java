package de.iosl.blockchain.identity.core.provider.account;

import de.iosl.blockchain.identity.core.shared.account.AccountService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProviderAccountService extends AccountService {

    @Override
    public String register(String password) throws IOException {
        return register(password, false);
    }
}
