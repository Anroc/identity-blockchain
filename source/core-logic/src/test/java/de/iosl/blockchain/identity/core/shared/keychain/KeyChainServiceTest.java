package de.iosl.blockchain.identity.core.shared.keychain;

import de.iosl.blockchain.identity.core.shared.keychain.data.KeyInfo;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyChainServiceTest {

    public static final String PATH = "./keyChain.json";
    public static final String PASSWD = "Pimp my Peon!";

    private KeyChainService keyChainService;
    private File file;

    @Before
    public void setup() {
        keyChainService = new KeyChainService();
        file = Paths.get(PATH).toFile();
        cleanUp();

        assertThat(file).doesNotExist();
    }

    @After
    public void cleanUp() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void saveAndLoadKeyChainTest() throws IOException {
        KeyPair keyPair = CryptEngine.generate().string().rsa()
                .getAsymmetricCipherKeyPair();

        String accountPath = "/home/musterman/.ethereum";

        keyChainService.saveKeyChain(keyPair, PATH, PASSWD, accountPath);

        assertThat(file)
                .exists()
                .isFile()
                .canRead()
                .canWrite()
                .hasExtension("json");

        KeyInfo res = keyChainService.readKeyChange(PATH, PASSWD);

        assertThat(res.getKeyPair()).isEqualToComparingFieldByField(keyPair);
        assertThat(res.getAccountPath()).isEqualTo(accountPath);
        assertThat(file).exists();
    }
}