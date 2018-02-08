package de.iosl.blockchain.identity.core.shared.api.permission;

import de.iosl.blockchain.identity.core.shared.BasicMockSuite;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.ObjectSymmetricCryptEngine;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClosureContentCryptEngineTest extends BasicMockSuite {

    private ClosureContentCryptEngine closureContentCryptEngine;

    private final KeyConverter keyConverter = new KeyConverter();
    private final ObjectSymmetricCryptEngine osce = new ObjectSymmetricCryptEngine();

    @Before
    public void setup() {
        this.closureContentCryptEngine = new ClosureContentCryptEngine();
    }

    @Test
    public void encryptAndDecrypt() {
        AsymmetricCryptEngine<String> cryptEngine = CryptEngine.generate().string().rsa();
        String userPublicKey = keyConverter.from(
                cryptEngine.getPublicKey()
        ).toBase64();

        final String staticValue1 = "Hans";
        final LocalDateTime staticValue2 = LocalDateTime.now();

        ClosureContractRequest ccr1 = ClosureContractRequest.init(
                "GIVEN_NAME",
                ClaimOperation.EQ,
                staticValue1
        );
        ClosureContractRequest ccr2 = ClosureContractRequest.init(
                "BIRTHDAY",
                ClaimOperation.EQ,
                staticValue2
        );

        Set<ClosureContractRequest> ccrs = Arrays.stream(new ClosureContractRequest[]{ccr1, ccr2}).collect(Collectors.toSet());

        // encrypt
        ClosureContent closureContent = closureContentCryptEngine.encrypt(userPublicKey, ccrs);

        // verify
        assertThat(closureContent.getEncryptedKey()).isNotEqualTo(userPublicKey);
        assertThat(closureContent.getEncryptedRequests()).isNotEmpty().hasSize(2);
        Set<ClosureContractRequest> blockchainCCRS = closureContent.getEncryptedRequests().stream()
                .map(string -> osce.deserializeFromBase64String(string, ClosureContractRequest.class))
                .collect(Collectors.toSet());
        assertThat(blockchainCCRS).usingElementComparatorIgnoringFields("staticValue").containsExactlyInAnyOrder(ccr1, ccr2);
        Set<String> encryptedValues = blockchainCCRS.stream()
                .map(ccr -> ccr.getClosureContractRequestPayload().getStaticValue().getUnifiedValueAs(String.class))
                .collect(Collectors.toSet());
        assertThat(encryptedValues).doesNotContain(
                staticValue1,
                staticValue2.toString());

        //decrypt
        PrivateKey privateKey = cryptEngine.getPrivateKey();
        Set<ClosureContractRequest> ccrsExtracted = closureContentCryptEngine.decrypt(closureContent, privateKey);

        // values got replaces.. restore them
        ccr1.getClosureContractRequestPayload().setStaticValue(new ValueHolder(staticValue1));
        ccr2.getClosureContractRequestPayload().setStaticValue(new ValueHolder(staticValue2));

        assertThat(ccrsExtracted).containsExactlyInAnyOrder(ccr1, ccr2);
    }

}