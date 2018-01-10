package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ActiveProfiles("test")
public class TransferEther {

    @Autowired
    private BlockchainAccess blockchainAccess;

    @Ignore
    @Test
    public void sendMoneyPlxGreetGov() throws Exception{

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

        TransactionReceipt transactionReceiptTransferEther= Web3jUtils
                .transferWeiFromCoinbaseToCreatedAccount(governmentAccount, Web3jConstants.amountToEther(Web3jConstants.GOV_MONEY_FROM_COAINBASE),blockchainAccess.getWeb3j());
        assertThat(transactionReceiptTransferEther.getStatus().equals("1"));
    }
}
