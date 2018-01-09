package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public interface EBAInterface {
    Account createWallet(String password, Path path);
    Account accessWallet(String pw, File file);
    Optional<String> deployRegistrarContract(Account account);
    Optional<TransactionReceipt> setApproval(Account governmentAccount, String contractAddress, boolean decision);
}
