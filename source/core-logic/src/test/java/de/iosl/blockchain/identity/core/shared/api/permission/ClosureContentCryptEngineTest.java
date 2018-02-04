package de.iosl.blockchain.identity.core.shared.api.permission;

import de.iosl.blockchain.identity.core.shared.BasicMockSuite;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.ObjectSymmetricCryptEngine;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClosureContentCryptEngineTest extends BasicMockSuite {

    private ClosureContentCryptEngine closureContentCryptEngine;

    private final KeyConverter keyConverter = new KeyConverter();

    @Before
    public void setup() {
        this.closureContentCryptEngine = new ClosureContentCryptEngine();
    }

    @Test
    public void buildClosureContent() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        AsymmetricCryptEngine<String> cryptEngine = CryptEngine.generate().string().rsa();
        String userPublicKey = keyConverter.from(
                cryptEngine.getPublicKey()
        ).toBase64();

        ClosureContractRequest ccr1 = ClosureContractRequest.init(
                "GIVEN_NAME",
                ClaimOperation.EQ,
                "Hans"
        );
        ClosureContractRequest ccr2 = ClosureContractRequest.init(
                "BIRTHDAY",
                ClaimOperation.EQ,
                LocalDateTime.now()
        );

        Set<ClosureContractRequest> ccrs = Arrays.stream(new ClosureContractRequest[]{ccr1, ccr2}).collect(Collectors.toSet());

        ClosureContent closureContent = closureContentCryptEngine.encrypt(userPublicKey, ccrs);

        assertThat(closureContent.getEncryptedKey()).isNotEqualTo(userPublicKey);
        assertThat(closureContent.getEncryptedRequests()).isNotEmpty().hasSize(2);

        //decrypt
        Key privateKey = cryptEngine.getPrivateKey();
        String secret = cryptEngine.decrypt(closureContent.getEncryptedKey(), privateKey);
        Key sharedKey = keyConverter.from(secret).toSymmetricKey();

        ObjectSymmetricCryptEngine objectSymmetricCryptEngine = new ObjectSymmetricCryptEngine();

        Set<ClosureContractRequest> ccrsExtracted = closureContent.getEncryptedRequests().stream()
                .map(content -> {
                    try {
                        return objectSymmetricCryptEngine.decryptAndCast(content, sharedKey, ClosureContractRequest.class);
                    } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet());
        assertThat(ccrsExtracted).containsExactlyInAnyOrder(ccr1, ccr2);
    }

}