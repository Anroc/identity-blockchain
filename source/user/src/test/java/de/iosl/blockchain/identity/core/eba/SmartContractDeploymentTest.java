package de.iosl.blockchain.identity.core.eba;

import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.eba.BlockchainAccess;
import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static de.iosl.blockchain.identity.core.shared.keychain.KeyChainService.WALLET_DIR;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SmartContractDeploymentTest {

    @Autowired
    BlockchainAccess blockchainAccess;


    @Test
    public void createAccountAndDeployRegistrarContract() throws Exception {

        String password = "password";
        String pathToFile = WALLET_DIR+"wallet"+ File.separator;
        Path path = Paths.get(pathToFile);

        blockchainAccess.setAccountAccess(new AccountAccess());
        Account account = blockchainAccess.createWallet(password, path);

        blockchainAccess.deployRegistrarContract(password, account);
    }

}
