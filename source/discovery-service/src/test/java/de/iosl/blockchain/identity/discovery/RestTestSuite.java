package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import de.iosl.blockchain.identity.discovery.hearthbeat.db.BeatDB;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.db.RegistryEntryDB;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;

public abstract class RestTestSuite {

    public static final String FILE = "sample_wallet.json";
    public static Credentials credentials;
    public static String ETH_ID;
    public static String PUBLIC_KEY;
    public static EthereumSigner ALGORITHM;

    @Autowired
    public BeatDB beatDB;
    @Autowired
    public RegistryEntryDB registryEntryDB;
    @Autowired
    public TestRestTemplate restTemplate;

    @BeforeClass
    public static void init() throws Exception {
        ClassPathResource resource = new ClassPathResource(FILE);
        File file = resource.getFile();

        credentials = WalletUtils.loadCredentials("asd", file);

        ETH_ID = Numeric.prependHexPrefix(
                Keys.getAddress(credentials.getEcKeyPair().getPublicKey()));
        PUBLIC_KEY = "SOME_PUBLIC_KEY";

        ALGORITHM = new EthereumSigner();
    }

    @After
    @Before
    public void capDatabase() {
        beatDB.deleteAll(Beat.class);
        registryEntryDB.deleteAll(RegistryEntry.class);
    }
}
