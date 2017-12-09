package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.AccountAccess;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Data
@Component
public class BlockchainAccess implements EBAInterface {

    @Autowired
    private AccountAccess accountAccess;

    @Override
    public Account createWallet(@NonNull String password, Path path) {
        return accountAccess.createAccount(password, path);
    }

    @Override
    public Account accessWallet(String pw, File file) {
        return accountAccess.accessWallet(pw, file);
    }


}
