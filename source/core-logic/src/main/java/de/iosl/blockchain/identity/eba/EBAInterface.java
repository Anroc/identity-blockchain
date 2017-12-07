package de.iosl.blockchain.identity.eba;

import de.iosl.blockchain.identity.eba.main.Account;

public interface EBAInterface {

    Account createWallet(String password);
    Account accessWallet(String pw, String walletName);

}
