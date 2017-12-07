package de.iosl.blockchain.identity.eba;

import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class BlockchainAccess implements EBAInterface {

    @Autowired
    private AccountAccess accountAccess;

    @Override
    public Account createWallet(@NonNull String password) {
        return accountAccess.createAccount(password);
    }

    @Override
    public Account accessWallet(@NonNull String password, @NonNull String walletName) {
        return accountAccess.accessWallet(password, walletName);
    }
}
