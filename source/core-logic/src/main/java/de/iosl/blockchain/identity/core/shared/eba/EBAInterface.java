package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.eba.main.Account;

import java.io.File;
import java.nio.file.Path;

public interface EBAInterface {
    Account createWallet(String password, Path path);
    Account accessWallet(String pw, File file);
    String deployRegistrarContract(Account account);
    void setRegisterApproval(Account governmentAccount, String contractAddress, boolean decision);
    boolean getRegisterApproval(Account account, String smartContractAddress);
}
