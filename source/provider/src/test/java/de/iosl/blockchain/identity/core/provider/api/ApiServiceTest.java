package de.iosl.blockchain.identity.core.provider.api;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.BasicMockSuite;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.ObjectSymmetricCryptEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiServiceTest extends BasicMockSuite {

    @Mock
    private EBAInterface ebaInterface;
    @Mock
    private KeyChain keyChain;
    @Mock
    private UserService userService;
    @Mock
    private HeartBeatService heartBeatService;
    @InjectMocks
    private ApiService apiService;

    private KeyConverter keyConverter;

    @Before
    public void setup() {
        this.keyConverter = new KeyConverter();
    }
    
    @Test
    public void buildClosureContent() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        AsymmetricCryptEngine<String> cryptEngine = CryptEngine.generate().string().rsa();
        String userPublicKey = keyConverter.from(
                cryptEngine.getPublicKey()
        ).toBase64();

        ClosureContractRequest ccr1 = new ClosureContractRequest(
                "GIVEN_NAME",
                ClaimOperation.EQ,
                "Hans"
        );
        ClosureContractRequest ccr2 = new ClosureContractRequest(
                "BIRTHDAY",
                ClaimOperation.EQ,
                LocalDateTime.now()
        );

        Set<ClosureContractRequest> ccrs = Sets.newHashSet(ccr1, ccr2);

        ClosureContent closureContent = apiService.buildCloseContent(userPublicKey, ccrs);

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
