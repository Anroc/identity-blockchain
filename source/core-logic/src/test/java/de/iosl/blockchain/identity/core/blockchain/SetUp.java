package de.iosl.blockchain.identity.core.blockchain;

import de.iosl.blockchain.identity.eba.BlockchainAccess;
import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static de.iosl.blockchain.identity.core.shared.keychain.KeyChainService.WALLET_DIR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@Slf4j
public class SetUp extends AbstractEthereumTest{


    Account newAccount;
    @After
    public void deleteCreatedWallet(){
        newAccount.getFile().deleteOnExit();
    }
    @Test
    public void testCreateAccountFromScratch() throws Exception {

        String password = "password";
        String pathToFile = WALLET_DIR+"wallet"+ File.separator;
        Path path = Paths.get(pathToFile);
        BlockchainAccess blockchainAccess = new BlockchainAccess();
        blockchainAccess.setAccountAccess(new AccountAccess());
        Account account = blockchainAccess.createWallet(password, path);
        this.newAccount = account;
        assertThat(account.getFile()).exists();

    }


}
