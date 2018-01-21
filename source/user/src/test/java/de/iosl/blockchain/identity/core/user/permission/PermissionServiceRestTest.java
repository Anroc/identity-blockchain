package de.iosl.blockchain.identity.core.user.permission;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
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
import java.util.List;
import java.util.Set;

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

    @SpyBean
    private KeyChain keyChain;

    @Autowired
    private PermissionService permissionService;

    @BeforeClass
    public static void init() throws IOException, CipherException {
        USER_CREDENTIALS = loadWallet(USER_FILE, WALLET_PW);
    }

    @Before
    public void setup() {
        keyChain.setAccount(getAccountFromCredentials(USER_CREDENTIALS));
    }

    @Test
    public void handleNewPermissionRequest() {
        PermissionContractContent contractContent = new PermissionContractContent(requiredClaims, optionalClaims, providerEthID);

        doReturn(contractContent).when(ebaInterface).getPermissionContractContent(any(Account.class), eq(permissionContractAddress));

        permissionService.handleNewPermissionRequest(issuedProviderEthID, permissionContractAddress);

        List<Message> messages = messageDB.findMessagesBySeen(false);
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

}
