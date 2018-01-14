package de.iosl.blockchain.identity.core;

import de.iosl.blockchain.identity.core.provider.config.CredentialConfig;
import de.iosl.blockchain.identity.core.provider.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.provider.factories.UserFactory;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.user.db.UserDB;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import org.bouncycastle.util.encoders.Base64;
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

public class RestTestSuite {

    protected static final String USER_FILE = "user-wallet.json";
    protected static final String STATE_FILE = "state-wallet.json";
    protected static final String WALLET_PW = "asd";

    protected UserFactory userFactory = UserFactory.instance();
    protected ClaimFactory claimFactory = ClaimFactory.instance();

    protected EthereumSigner signer = new EthereumSigner();

    @SpyBean
    public HeartBeatService heartBeatService;
    @SpyBean
    public EBAInterface ebaInterface;
    @Autowired
    public TestRestTemplate restTemplate;
    @Autowired
    protected UserDB userDB;
    @Autowired
    private CredentialConfig credentialConfig;

    public String getAuthentication() {
        return "Basic " +
                new String(Base64.encode((credentialConfig.getUsername() + ":" + credentialConfig.getPassword()).getBytes()));
    }

    @After
    @Before
    public void capDatabase() {
        userDB.deleteAll(User.class);
    }

    public static Credentials loadWallet(String name, String pw) throws IOException, CipherException {
        return WalletUtils.loadCredentials(pw, loadFile(name));
    }

    public static File loadFile(String name) throws IOException {
        ClassPathResource resource = new ClassPathResource(name);
        return resource.getFile();
    }

    public ECSignature getSignature(Object paylaod, Credentials credentials) {
        return ECSignature.fromSignatureData(
                signer.sign(paylaod, credentials.getEcKeyPair())
        );
    }
}
