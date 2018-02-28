package de.iosl.blockchain.identity.core.shared;

import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.KeyPair;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class KeyChain {

    public static final String WALLET_DIR =
            System.getProperty("user.home") + File.separator
                    + ".ethereum" + File.separator + "blockchain-identity"
                    + File.separator;
    public static final String KEY_CHAIN = "keychain.json";
    public static final String GOV_WALLET = "gov-wallet.json";

    private Account account;
    private KeyPair rsaKeyPair;

    private String registerSmartContractAddress;

    private boolean registered;

    public boolean isActive() {
        return account != null;
    }
}
