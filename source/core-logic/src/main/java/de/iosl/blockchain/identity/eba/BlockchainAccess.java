package de.iosl.blockchain.identity.eba;

import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import de.iosl.blockchain.identity.eba.main.DeployContract;
import de.iosl.blockchain.identity.eba.main.util.Web3jConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

@Slf4j
@Data
@Component
public class BlockchainAccess implements EBAInterface {

    @Autowired
    private AccountAccess accountAccess;

    @Autowired
    private DeployContract deployContract;

    @Autowired
    private Web3j web3j;

    @Bean
    public Web3j buildHttpClientByParams(Web3jConstants constants) {
        String url = String.format("http://%s:%s", constants.getAddress(), constants.getPort());
        log.debug(url);
        return Web3j.build(new HttpService(url));
    }

    @Override
    public Account createWallet(@NonNull String password, Path path) {
        return accountAccess.createAccount(password, path);
    }

    @Override
    public Account accessWallet(String pw, File file) {
        return accountAccess.accessWallet(pw, file);
    }

    public void deployRegistrarContract(@NonNull String password, Account account){
        deployContract.deployRegistrarContract(password, account, web3j);
    }



}
