package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static de.iosl.blockchain.identity.core.shared.KeyChain.WALLET_DIR;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SmartContractDeploymentTest {

    @Autowired
    private BlockchainAccess blockchainAccess;

    List<Account> newAccounts = new ArrayList<Account>();
    String pathToFile = WALLET_DIR + "wallet" + File.separator;

    @After
    public void deleteCreatedWallet(){
        this.newAccounts.stream().forEach(account -> account.getFile().deleteOnExit());
    }

    @Test
    public void createAccountAndDeployRegistrarContract() throws Exception {

        String password = "password";
        Path path = Paths.get(pathToFile);

        Account newAccount = blockchainAccess.createWallet(password, path);
        assertThat(newAccount.getFile()).exists();
        this.newAccounts.add(newAccount);

        String registrarContractAddress = blockchainAccess.deployRegistrarContract(newAccount);
        assertThat(registrarContractAddress).isNotNull();

    }
    @Test
    public void setApprovalAsGovernmentTest() throws  Exception{
        String password = "password";
        Path path = Paths.get(pathToFile);

        Account newAccount = blockchainAccess.createWallet(password, path);
        assertThat(newAccount.getFile()).exists();
        this.newAccounts.add(newAccount);

        String registrarContractAddress = blockchainAccess.deployRegistrarContract(newAccount);
        assertThat(registrarContractAddress).isNotNull();
        Boolean decision = true;

        String govWalletName = "gov-wallet.json";
        String govPassword = "penispumpe";

        Credentials govCred = RestTestSuite.loadWallet(govWalletName, govPassword);
        Account governmentAccount =new Account (
                govCred.getAddress(),
                govCred.getEcKeyPair().getPublicKey(),
                govCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(govWalletName),
                govCred
        );

        TransactionReceipt transactionReceiptTransferEther= Web3jUtils.transferWeiFromCoinbaseToCreatedAccount(governmentAccount, Web3jConstants.amountToEther(Web3jConstants.GOV_MONEY_FROM_COAINBASE),blockchainAccess.getWeb3j());
        blockchainAccess.setRegisterApproval(governmentAccount, registrarContractAddress, decision);
        //add assert that approval is true now
    }
}
