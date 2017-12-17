package de.iosl.blockchain.identity.core.eba;

import de.iosl.blockchain.identity.core.shared.eba.BlockchainAccess;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.AccountAccess;
import de.iosl.blockchain.identity.core.user.Application;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthCoinbase;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static de.iosl.blockchain.identity.core.shared.KeyChain.WALLET_DIR;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SmartContractDeploymentTest {

    @Autowired
    BlockchainAccess blockchainAccess;

    Account newAccount;
    @After
    public void deleteCreatedWallet(){
        newAccount.getFile().deleteOnExit();
    }

    @Test
    public void createAccountAndDeployRegistrarContract() throws Exception {
        EthAccounts accountsResponse  = blockchainAccess.getWeb3j().ethAccounts().sendAsync().get();
        List<String> accounts = accountsResponse.getAccounts();
        log.info("Account size:"+accounts.size()+"");

        String password = "password";
        String pathToFile = WALLET_DIR+"wallet"+ File.separator;
        Path path = Paths.get(pathToFile);

        this.newAccount= blockchainAccess.createWallet(password, path);

        blockchainAccess.deployRegistrarContract(password, newAccount);

        accountsResponse  = blockchainAccess.getWeb3j().ethAccounts().sendAsync().get();
        accounts = accountsResponse.getAccounts();
        log.info("Account size:"+accounts.size()+"");    }

}
