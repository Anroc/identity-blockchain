package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;

import static de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils.getBalanceWei;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ActiveProfiles("test")
public class TransferEther {

    @Autowired
    private BlockchainAccess blockchainAccess;
    private Account governmentAccount;

    @Before
    public void setUp() throws IOException, CipherException {
        String govWalletName = "gov-wallet.json";
        String govPassword = "penispumpe";

        Credentials govCred = RestTestSuite.loadWallet(govWalletName, govPassword);
        this.governmentAccount=new Account (
                govCred.getAddress(),
                govCred.getEcKeyPair().getPublicKey(),
                govCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(govWalletName),
                govCred
        );
    }
    @Ignore
    @Test
    public void sendMoneyPlxGreetGov() throws Exception{
        log.info("wallet balance before deployment {}", getBalanceWei(blockchainAccess.getWeb3j(), governmentAccount.getAddress()));
        TransactionReceipt transactionReceiptTransferEther= Web3jUtils
                .transferWeiFromCoinbaseToCreatedAccount(governmentAccount, Web3jConstants.amountToEther(Web3jConstants.GOV_MONEY_FROM_COAINBASE),blockchainAccess.getWeb3j());
        log.info("wallet balance after deployment {}", getBalanceWei(blockchainAccess.getWeb3j(), governmentAccount.getAddress()));
        assertThat(transactionReceiptTransferEther.getStatus().equals("1"));
    }

    @Ignore
    @Test
    public void getGovMoney() throws Exception{
        Web3j web3j = blockchainAccess.getWeb3j();
        String address =governmentAccount.getAddress();

        BigInteger amount = getBalanceWei(web3j,address);
        log.info("wallet balance {}", amount);
    }
}
