package de.iosl.blockchain.identity.core.provider.permission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.iosl.blockchain.identity.core.BasicMockSuite;
import de.iosl.blockchain.identity.core.provider.api.client.APIProviderService;
import de.iosl.blockchain.identity.core.provider.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.provider.factories.UserFactory;
import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.user.UserService;
import de.iosl.blockchain.identity.core.provider.user.data.PermissionGrand;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractResponse;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.message.MessageService;
import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import org.assertj.core.data.MapEntry;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionRequestServiceTest extends BasicMockSuite {

    @Mock private APIProviderService apiProviderService;
    @Mock private UserService userService;
    @Mock private MessageService messageService;
    @Mock private EBAInterface ebaInterface;
    @Mock private KeyChain keyChain;
    @Mock private HeartBeatService heartBeatService;

    @Spy
    @InjectMocks
    private PermissionRequestService permissionRequestService;

    private final ClaimFactory claimFactory = ClaimFactory.instance();
    private final UserFactory userFactory = UserFactory.instance();

    private final String userEthID = "0x123";
    private final String providerEthID = "0x456";
    private final String url = "https://postbank.de:4321";
    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final List<String> requiredClaims = Lists.newArrayList(claimID_givenName, claimID_familyName);
    private final List<String> optionalClaims = Lists.newArrayList(claimID_age);
    private final String permissionContractAddress = "0xabc";
    private final List<ProviderClaim> receivedClaims = Lists.newArrayList(claimFactory.create(claimID_familyName), claimFactory.create(claimID_givenName), claimFactory.create(claimID_age));
    private final List<ClaimDTO> receivedClaimDTOs = receivedClaims.stream().map(ClaimDTO::new).collect(Collectors.toList());

    private User user;

    @Before
    public void setup() {
        user = userFactory.create();

        doReturn(permissionContractAddress).when(apiProviderService)
                .requestUserClaims(any(PermissionRequest.class));
    }

    @Test
    public void requestPermission_updatingUser() {
        PermissionRequest permissionRequest = new PermissionRequest(userEthID, url, requiredClaims, optionalClaims, Lists.newArrayList());

        doReturn(Optional.of(user)).when(userService).findUserByEthID(userEthID);
        doAnswer(returnsFirstArg()).when(userService).updateUser(any(User.class));
        doNothing().when(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);

        permissionRequestService.requestPermission(permissionRequest);

        verify(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);
    }

    @Test
    public void requestPermission_creatingUser() {
        PermissionRequest permissionRequest = new PermissionRequest(userEthID, url, requiredClaims, optionalClaims, Lists.newArrayList());

        doReturn(Optional.empty()).when(userService).findUserByEthID(userEthID);
        doAnswer(returnsFirstArg()).when(userService).insertUser(any(User.class));
        doNothing().when(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);

        permissionRequestService.requestPermission(permissionRequest);

        verify(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);
    }

    @Test
    public void registerPermissionContractListener() {
        Map<String, String> requiredClaimResult = new HashMap<>();
        Map<String, String> optionalClaimResult = new HashMap<>();
        requiredClaimResult.put(claimID_givenName, buildApprovedClaim(claimID_givenName, providerEthID, userEthID));
        requiredClaimResult.put(claimID_familyName, buildApprovedClaim(claimID_familyName, providerEthID, userEthID));
        optionalClaimResult.put(claimID_age, buildApprovedClaim(claimID_age, providerEthID, userEthID));

        user.putPermissionGrant(PermissionGrand.init(permissionContractAddress, requiredClaims, optionalClaims, Lists.newArrayList()));
        assertThat(user.getPermissionGrands().get(0).getRequiredClaimGrants())
                .containsOnlyKeys(claimID_givenName, claimID_familyName).doesNotContainValue(true);
        assertThat(user.getPermissionGrands().get(0).getOptionalClaimGrants())
                .containsOnlyKeys(claimID_age).doesNotContainValue(true);

        Account account = mock(Account.class);
        ECSignatureValidator validator = mock(ECSignatureValidator.class);

        PermissionContractContent pcc = new PermissionContractContent(new HashSet(requiredClaims), new HashSet(optionalClaims), providerEthID);
        PermissionContractResponse permissionContractResponse = new PermissionContractResponse( receivedClaimDTOs, Lists.newArrayList());
        doReturn(pcc).when(ebaInterface).getPermissionContractContent(account, permissionContractAddress);
        doReturn(account).when(keyChain).getAccount();
        doReturn(true).when(keyChain).isActive();
        doReturn(validator).when(permissionRequestService).getEcSignatureValidator();
        doReturn(true).when(validator).isRequestValid(any(SignedRequest.class));
        doReturn(permissionContractResponse).when(apiProviderService).requestClaimsForPPR(eq(url), eq(userEthID), eq(permissionContractAddress), anyList(), anyList());
        doReturn(Optional.of(user)).when(userService).findUserByEthID(userEthID);
        doAnswer(returnsFirstArg()).when(userService).updateUser(any(User.class));
        doReturn(mock(Message.class)).when(messageService).createMessage(MessageType.PERMISSION_REQUEST, user.getId(), null);

        permissionRequestService.permissionContractUpdateHandler(permissionContractAddress, userEthID, url);

        assertThat(user.getPermissionGrands()).hasSize(1);
        PermissionGrand permissionGrand = user.getPermissionGrands().get(0);
        assertThat(permissionGrand.getPermissionContractAddress()).isEqualTo(permissionContractAddress);
        assertThat(permissionGrand.getOptionalClaimGrants()).containsOnly(MapEntry.entry(claimID_age, true));
        assertThat(permissionGrand.getRequiredClaimGrants()).containsOnly(MapEntry.entry(claimID_givenName, true), MapEntry.entry(claimID_familyName, true));
        verify(messageService).createMessage(MessageType.NEW_CLAIMS, user.getId(), null);
        verify(apiProviderService).requestClaimsForPPR(eq(url), eq(userEthID), eq(permissionContractAddress), anyList(), anyList());
    }

    private String buildApprovedClaim(String claimID, String providerEthID, String userEthID) {
        ApprovedClaim approvedClaim = new ApprovedClaim(userEthID, claimID, providerEthID);
        SignedRequest<ApprovedClaim> signedRequest = new SignedRequest<>(
                approvedClaim,
                new ECSignature(base64Decode("3"), base64Decode("5"), (byte) 7)
        );

        return base64Decode(objectToJson(signedRequest));
    }

    private String base64Decode(String value) {
        return new String(Base64.encode(value.getBytes()));
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
