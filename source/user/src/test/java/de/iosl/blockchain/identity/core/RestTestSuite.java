package de.iosl.blockchain.identity.core;

import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.db.MessageDB;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.db.UserClaimDB;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.user.permission.db.PermissionRequestDB;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class RestTestSuite {

    protected static final String USER_FILE = "user-wallet.json";
    protected static final String WALLET_PW = "asd";

    @SpyBean
    protected EBAInterface ebaInterface;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected MessageDB messageDB;
    @Autowired
    protected UserClaimDB userClaimDB;
    @Autowired
    protected PermissionRequestDB permissionRequestDB;

    @After
    @Before
    public void capDatabase() {
        messageDB.deleteAll(Message.class);
        userClaimDB.deleteAll(UserClaim.class);
        permissionRequestDB.deleteAll(PermissionRequest.class);
    }

    public static Credentials loadWallet(String name, String pw) throws IOException, CipherException {
        return WalletUtils.loadCredentials(pw, loadFile(name));
    }

    public static File loadFile(String name) throws IOException {
        ClassPathResource resource = new ClassPathResource(name);
        return resource.getFile();
    }

    public Account getAccountFromCredentials(Credentials credentials) {
        return new Account(
                credentials.getAddress(),
                credentials.getEcKeyPair().getPublicKey(),
                credentials.getEcKeyPair().getPrivateKey(),
                Paths.get(("/")).toFile(),
                credentials);
    }
}
