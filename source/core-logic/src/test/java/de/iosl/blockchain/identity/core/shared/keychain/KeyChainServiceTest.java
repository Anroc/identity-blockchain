package de.iosl.blockchain.identity.core.shared.keychain;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.keychain.data.KeyInfo;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
        final String scAddress = "0x123";

        KeyChain keyChain = new KeyChain(
                new Account(
                        "asd",
                        BigInteger.TEN,
                        BigInteger.TEN,
                        Paths.get(accountPath).toFile(),
                        mock(Credentials.class)
                ), keyPair, scAddress, false);


        keyChainService.saveKeyChain(keyChain, PASSWD, PATH);

        assertThat(file)
                .exists()
                .isFile()
                .canRead()
                .canWrite()
                .hasExtension("json");

        KeyInfo res = keyChainService.readKeyChange(PATH, PASSWD);

        assertThat(res.getKeyPair()).isEqualToComparingFieldByField(keyPair);
        assertThat(res.getRegisterSmartContractAddress()).isEqualTo(scAddress);
        assertThat(res.getAccountPath()).isEqualTo(accountPath);
        assertThat(file).exists();
    }
}