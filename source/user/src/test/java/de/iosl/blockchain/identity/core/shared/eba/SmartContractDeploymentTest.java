package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.eba.BlockchainAccess;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.user.Application;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.iosl.blockchain.identity.core.shared.KeyChain.WALLET_DIR;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SmartContractDeploymentTest {

    @Autowired
    BlockchainAccess blockchainAccess;

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

        Optional<String> registrarContractAddress = blockchainAccess.deployRegistrarContract(newAccount);
        assertThat(registrarContractAddress).isPresent();

    }
    @Test
    public void setApprovalAsGovernmentTest() throws  Exception{
        String password = "password";
        Path path = Paths.get(pathToFile);

        Account newAccount = blockchainAccess.createWallet(password, path);
        assertThat(newAccount.getFile()).exists();
        this.newAccounts.add(newAccount);

        Optional<String> registrarContractAddress = blockchainAccess.deployRegistrarContract(newAccount);
        assertThat(registrarContractAddress).isPresent();
        Boolean decision = true;
        String govPassword = "penispumpe";
        Account governmentAccount =blockchainAccess.accessWallet(
                govPassword,
                new File(pathToFile+"gov-wallet.json")
        );
//        TransactionReceipt transactionReceiptTransferEther= Web3jUtils.transferWeiFromCoinbaseToCreatedAccount(governmentAccount, Web3jConstants.DEFAULT_START_ETHER,blockchainAccess.getWeb3j());
        Optional<TransactionReceipt> transactionReceipt= blockchainAccess.setApproval(governmentAccount, registrarContractAddress, decision);
        assertThat(transactionReceipt).isPresent();
    }


}