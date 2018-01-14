package de.iosl.blockchain.identity.crypt.sign;

import de.iosl.blockchain.identity.crypt.TestEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EthereumSignerTest {

    private static final String FILE = "sample_wallet.json";

    private ECKeyPair eckeyPair;
    private EthereumSigner signer;

    @Before
    public void loadWallet() throws IOException, CipherException {
        signer = new EthereumSigner();

        ClassPathResource resource = new ClassPathResource(FILE);
        File file = resource.getFile();

        Credentials credentials = WalletUtils.loadCredentials("asd", file);
        eckeyPair = credentials.getEcKeyPair();
    }

    @Test
    public void signAndVerifySignature() {
        TestEntity testEntity = new TestEntity("field", 1337);

        Sign.SignatureData data = signer.sign(testEntity, eckeyPair);
        boolean isValid = signer
                .verifySignature(testEntity, data, eckeyPair.getPublicKey());

        assertThat(isValid).isTrue();
    }
}
