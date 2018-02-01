package de.iosl.blockchain.identity.core.provider.api;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.BasicMockSuite;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.JsonSymmetricCryptEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Set;

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

        ClosureContractRequestDTO ccr = new ClosureContractRequestDTO(
                "GIVEN_NAME",
                ClaimOperation.EQ,
                "Hans"
        );
        Set<ClosureContractRequestDTO> ccrs = Sets.newHashSet(ccr);

        ClosureContent closureContent = apiService.buildCloseContent(userPublicKey, ccrs);

        assertThat(closureContent.getEncryptedKey()).isNotEqualTo(userPublicKey);
        assertThat(closureContent.getEncryptedRequests()).isNotEmpty().hasSize(1);

        //decrypt
        Key privateKey = cryptEngine.getPrivateKey();
        String secret = cryptEngine.decrypt(closureContent.getEncryptedKey(), privateKey);
        Key sharedKey = keyConverter.from(secret).toSymmetricKey();

        JsonSymmetricCryptEngine jsonSymmetricCryptEngine = (JsonSymmetricCryptEngine) CryptEngine.from(sharedKey).json().aes();

        for(String content : closureContent.getEncryptedRequests()) {
            ClosureContractRequestDTO ccrExtracted = jsonSymmetricCryptEngine.decryptEntity(content, sharedKey, ClosureContractRequestDTO.class);
            assertThat(ccrExtracted).isEqualTo(ccr);
        }
    }
}
