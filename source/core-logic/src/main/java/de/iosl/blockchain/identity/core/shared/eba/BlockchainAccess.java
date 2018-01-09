package de.iosl.blockchain.identity.core.shared.eba;

import com.sun.istack.internal.Nullable;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.AccountAccess;
import de.iosl.blockchain.identity.core.shared.eba.main.RegistrarContractUtils;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Data
@Component
public class BlockchainAccess implements EBAInterface {

    @Autowired
    private AccountAccess accountAccess;

    @Autowired
    private RegistrarContractUtils registrarContractUtils;

    @Autowired
    private Web3j web3j;

    @Bean
    public Web3j buildHttpClientByParams(BlockchainIdentityConfig config) {
        String url = String.format("http://%s:%s", config.getEthereum().getAddress(), config.getEthereum().getPort());
        log.debug(url);
        return Web3j.build(new HttpService(url));
    }

    @Override
    public Account createWallet(@NonNull String password, Path path) {
        return accountAccess.createAccount(password, path, web3j);
    }

    @Override
    public Account accessWallet(String pw, File file) {
        return accountAccess.accessWallet(pw, file);
    }

    @Override
    public Optional<String> deployRegistrarContract(Account account){
        Contract contract = registrarContractUtils.deployRegistrarContract(account, web3j);
        return Optional.ofNullable(contract.getContractAddress());
    }

    @Override
    public Optional<TransactionReceipt> setApproval(Account governmentAccount, Optional<String> contractAddress, Boolean decision) {
        TransactionReceipt transactionReceipt = this.registrarContractUtils.approveRegistrarContractAsGovernment(governmentAccount,contractAddress,decision, web3j);
        return Optional.ofNullable(transactionReceipt);
    }
}
