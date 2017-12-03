package de.iosl.blockchain.identity.eba;

public interface EBAInterface {

    Account createWallet(String password);
    Account accessWallet(String pw);

}
