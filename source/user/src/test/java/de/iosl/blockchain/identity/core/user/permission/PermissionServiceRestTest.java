package de.iosl.blockchain.identity.core.user.permission;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.permission.ClosureContentCryptEngine;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.user.permission.data.ClosureRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PermissionServiceRestTest extends RestTestSuite {

    private static Credentials USER_CREDENTIALS;

    private final String providerEthID = "0x123";
    private final String issuedProviderEthID = "0x456";
    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final Set<String> requiredClaims = Sets.newHashSet(claimID_givenName, claimID_familyName);
    private final Set<String> optionalClaims = Sets.newHashSet(claimID_age);
    private final String permissionContractAddress = "0xabc";

    private final ClaimFactory claimFactory = new ClaimFactory();

    @SpyBean
    private KeyChain keyChain;

    @Autowired
    private PermissionService permissionService;

    private final ClosureContentCryptEngine closureContentCryptEngine = new ClosureContentCryptEngine();

    @BeforeClass
    public static void init() throws IOException, CipherException {
        USER_CREDENTIALS = loadWallet(USER_FILE, WALLET_PW);
    }

    @Before
    public void setup() {
        keyChain.setAccount(getAccountFromCredentials(USER_CREDENTIALS));
        keyChain.setRsaKeyPair(CryptEngine.generate().string().rsa().getAsymmetricCipherKeyPair());

        userClaimDB.insert(claimFactory.create(claimID_givenName, ClaimType.STRING, "Hans", keyChain.getAccount().getAddress()));
        userClaimDB.insert(claimFactory.create(claimID_familyName, ClaimType.STRING, "Wurst", keyChain.getAccount().getAddress()));
        userClaimDB.insert(claimFactory.create(claimID_age, ClaimType.DATE, LocalDateTime.of(2000,04,11,12,12,12), keyChain.getAccount().getAddress()));
    }

    @Test
    public void handleNewPermissionRequest() {
        PermissionContractContent contractContent = new PermissionContractContent(requiredClaims, optionalClaims, providerEthID);

        doReturn(contractContent).when(ebaInterface).getPermissionContractContent(any(Account.class), eq(permissionContractAddress));

        permissionService.handleNewPermissionRequest(issuedProviderEthID, permissionContractAddress);

        List<Message> messages = messageDB.findAll();
        assertThat(messages).hasSize(1);
        Message message = messages.get(0);
        assertThat(message.getMessageType()).isEqualTo(MessageType.PERMISSION_REQUEST);
        assertThat(message.getUserId()).isNull();
        assertThat(message.getSubjectID()).isNotNull().isNotEmpty();

        String permissionRequestID = message.getSubjectID();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getRequestingProvider()).isEqualTo(providerEthID);
        assertThat(permissionRequest.getOptionalClaims().keySet()).isEqualTo(optionalClaims);
        assertThat(permissionRequest.getOptionalClaims()).doesNotContainValue(true);
        assertThat(permissionRequest.getRequiredClaims().keySet()).isEqualTo(requiredClaims);
        assertThat(permissionRequest.getRequiredClaims()).doesNotContainValue(true);
        assertThat(permissionRequest.getPermissionContractAddress()).isEqualTo(permissionContractAddress);
        assertThat(permissionRequest.getIssuedProvider()).isEqualTo(issuedProviderEthID);
    }

    @Test
    public void handleNewClosureRequest() {
        final String staticValue1 = "Hans";
        final LocalDateTime staticValue2 = LocalDateTime.of(2015, 4, 1, 12, 12).minus(18L, ChronoUnit.YEARS);

        ClosureContractRequest closureContractRequest1 = ClosureContractRequest.init(claimID_givenName, ClaimOperation.EQ, staticValue1);
        ClosureContractRequest closureContractRequest2 = ClosureContractRequest.init(
                claimID_age,
                ClaimOperation.LE,
                staticValue2
        );

        PermissionContractContent contractContent = new PermissionContractContent(
                Sets.newHashSet(),
                Sets.newHashSet(),
                providerEthID,
                generateEncryptedClosureContentRequest(Sets.newHashSet(closureContractRequest1, closureContractRequest2))
        );

        doReturn(contractContent).when(ebaInterface).getPermissionContractContent(any(Account.class), eq(permissionContractAddress));

        permissionService.handleNewPermissionRequest(issuedProviderEthID, permissionContractAddress);

        List<Message> messages = messageDB.findAll();
        assertThat(messages).hasSize(1);
        Message message = messages.get(0);
        assertThat(message.getMessageType()).isEqualTo(MessageType.PERMISSION_REQUEST);
        assertThat(message.getUserId()).isNull();
        assertThat(message.getSubjectID()).isNotNull().isNotEmpty();

        String permissionRequestID = message.getSubjectID();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getRequestingProvider()).isEqualTo(providerEthID);
        assertThat(permissionRequest.getOptionalClaims()).isEmpty();
        assertThat(permissionRequest.getRequiredClaims()).isEmpty();
        assertThat(permissionRequest.getPermissionContractAddress()).isEqualTo(permissionContractAddress);
        assertThat(permissionRequest.getIssuedProvider()).isEqualTo(issuedProviderEthID);

        assertThat(permissionRequest.getClosureRequests())
                .usingElementComparatorIgnoringFields("description", "staticValue")
                .containsExactlyInAnyOrder(
                        new ClosureRequest(
                                closureContractRequest1,
                                "",
                                true
                                ),
                        new ClosureRequest(
                                closureContractRequest2,
                                "",
                                false
                        ));

        Set<Object> staticValues = permissionRequest.getClosureRequests()
                .stream()
                .map(ClosureRequest::getStaticValue)
                .map(ValueHolder::getUnifiedValue)
                .collect(Collectors.toSet());
        assertThat(staticValues).containsExactlyInAnyOrder(staticValue1, staticValue2);
    }

    private ClosureContent generateEncryptedClosureContentRequest(Set<ClosureContractRequest> closureContractRequest) {
        AsymmetricCryptEngine<String> asymmetricCryptEngine = CryptEngine.from(keyChain.getRsaKeyPair()).string().rsa();

        Key publicKey = asymmetricCryptEngine.getPublicKey();
        String base64Key = new KeyConverter().from(publicKey).toBase64();
        return closureContentCryptEngine.encrypt(base64Key, closureContractRequest);

    }

}
