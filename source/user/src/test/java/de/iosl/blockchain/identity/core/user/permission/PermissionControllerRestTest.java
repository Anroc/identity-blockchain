package de.iosl.blockchain.identity.core.user.permission;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.ds.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.permission.data.ClosureRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequestDTO;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PermissionControllerRestTest extends RestTestSuite {

    private static Credentials USER_CREDENTIALS;

    private final String providerEthID = "0x123";
    private final String issuedProviderEthID = "0x456";
    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final Set<String> requiredClaims = Sets.newHashSet(claimID_givenName, claimID_familyName);
    private final Set<String> optionalClaims = Sets.newHashSet(claimID_age);
    private final String permissionContractAddress = "0xabc";

    private final String permissionRequestID = UUID.randomUUID().toString();

    private PermissionRequest permissionRequest;


    @SpyBean
    private KeyChain keyChain;
    @SpyBean
    private HeartBeatService heartBeatService;

    private EthereumSigner signer = new EthereumSigner();
    private ObjectMapper objectMapper = new ObjectMapper();
    private JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<SignedRequest<ApprovedClaim>>() {});


    @BeforeClass
    public static void init() throws IOException, CipherException {
        USER_CREDENTIALS = loadWallet(USER_FILE, WALLET_PW);
    }

    @Before
    public void setup() {
        permissionRequest = new PermissionRequest(
                permissionRequestID,
                providerEthID,
                issuedProviderEthID,
                permissionContractAddress,
                requiredClaims.stream().collect(Collectors.toMap(s -> s, s -> false)),
                optionalClaims.stream().collect(Collectors.toMap(s -> s, s -> false)),
                Sets.newHashSet()
        );

        permissionRequestDB.insert(permissionRequest);

        keyChain.setAccount(getAccountFromCredentials(USER_CREDENTIALS));
    }

    @Test
    public void updatePermissionRequest() {

        doReturn(mock(Beat.class)).when(heartBeatService)
                .createEthIdBeat(providerEthID, EventType.PPR_UPDATE, permissionContractAddress);

        doAnswer(invocation -> {
            PermissionContractContent permissionContractContent = invocation.getArgumentAt(2, PermissionContractContent.class);
            assertPPRContent(permissionContractContent.getOptionalClaims(), optionalClaims);
            assertPPRContent(permissionContractContent.getRequiredClaims(), requiredClaims);
            return null;
        }).when(ebaInterface).approvePermissionContract(any(Account.class), eq(permissionContractAddress), any(PermissionContractContent.class));


        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(permissionRequest);
        permissionRequestDTO.getOptionalClaims().keySet().forEach(key -> permissionRequestDTO.getOptionalClaims().put(key, true));
        permissionRequestDTO.getRequiredClaims().keySet().forEach(key -> permissionRequestDTO.getRequiredClaims().put(key, true));

        ResponseEntity<PermissionRequestDTO> result = restTemplate.exchange("/permissions/" + permissionRequestID,
                HttpMethod.PUT,
                new HttpEntity<>(permissionRequestDTO),
                PermissionRequestDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getRequiredClaims()).doesNotContainValue(false);
        assertThat(permissionRequest.getOptionalClaims()).doesNotContainValue(false);
    }

    @Test
    public void updatePermissionRequest_rejectingOptional() {

        doReturn(mock(Beat.class)).when(heartBeatService)
                .createEthIdBeat(providerEthID, EventType.PPR_UPDATE, permissionContractAddress);

        doAnswer(invocation -> {
            PermissionContractContent permissionContractContent = invocation.getArgumentAt(2, PermissionContractContent.class);
            permissionContractContent.getOptionalClaims().values().stream().forEach( sign -> assertThat(sign).isNull());
            assertPPRContent(permissionContractContent.getRequiredClaims(), requiredClaims);
            return null;
        }).when(ebaInterface).approvePermissionContract(any(Account.class), eq(permissionContractAddress), any(PermissionContractContent.class));


        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(permissionRequest);
        permissionRequestDTO.getOptionalClaims().keySet().forEach(key -> permissionRequestDTO.getOptionalClaims().put(key, false));
        permissionRequestDTO.getRequiredClaims().keySet().forEach(key -> permissionRequestDTO.getRequiredClaims().put(key, true));

        ResponseEntity<PermissionRequestDTO> result = restTemplate.exchange("/permissions/" + permissionRequestID,
                HttpMethod.PUT,
                new HttpEntity<>(permissionRequestDTO),
                PermissionRequestDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getRequiredClaims()).doesNotContainValue(false);
        assertThat(permissionRequest.getOptionalClaims()).doesNotContainValue(true);
    }

    @Test
    public void updatePermissionRequest_rejectionAll() {

        doReturn(mock(Beat.class)).when(heartBeatService)
                .createEthIdBeat(providerEthID, EventType.PPR_UPDATE, permissionContractAddress);

        doAnswer(invocation -> {
            PermissionContractContent permissionContractContent = invocation.getArgumentAt(2, PermissionContractContent.class);
            permissionContractContent.getOptionalClaims().values().stream().forEach( sign -> assertThat(sign).isNull());
            permissionContractContent.getRequiredClaims().values().stream().forEach( sign -> assertThat(sign).isNull());
            return null;
        }).when(ebaInterface).approvePermissionContract(any(Account.class), eq(permissionContractAddress), any(PermissionContractContent.class));


        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(permissionRequest);
        permissionRequestDTO.getOptionalClaims().keySet().forEach(key -> permissionRequestDTO.getOptionalClaims().put(key, false));
        permissionRequestDTO.getRequiredClaims().keySet().forEach(key -> permissionRequestDTO.getRequiredClaims().put(key, false));

        ResponseEntity<PermissionRequestDTO> result = restTemplate.exchange("/permissions/" + permissionRequestID,
                HttpMethod.PUT,
                new HttpEntity<>(permissionRequestDTO),
                PermissionRequestDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getRequiredClaims()).doesNotContainValue(true);
        assertThat(permissionRequest.getOptionalClaims()).doesNotContainValue(true);
    }

    @Test
    public void updatePermissionRequest_rejectingSomeRequiered() {
        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(permissionRequest);
        permissionRequestDTO.getOptionalClaims().keySet().forEach(key -> permissionRequestDTO.getOptionalClaims().put(key, true));
        permissionRequestDTO.getRequiredClaims().put(claimID_givenName, false);
        permissionRequestDTO.getRequiredClaims().put(claimID_familyName, true);

        ResponseEntity<PermissionRequestDTO> result = restTemplate.exchange("/permissions/" + permissionRequestID,
                HttpMethod.PUT,
                new HttpEntity<>(permissionRequestDTO),
                PermissionRequestDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updatePermissionRequest_closures() {
        KeyPair keyPair = CryptEngine.generate().string().rsa().getAsymmetricCipherKeyPair();
        KeyConverter keyConverter = new KeyConverter();

        doReturn(mock(Beat.class)).when(heartBeatService)
                .createEthIdBeat(providerEthID, EventType.PPR_UPDATE, permissionContractAddress);

        doNothing().when(ebaInterface)
                .approvePermissionContract(any(Account.class), eq(permissionContractAddress), any(PermissionContractContent.class));

        doReturn(Optional.of(new RegistryEntryDTO(keyConverter.from(keyPair.getPublic()).toBase64(), "exmaple.com", 123)))
                .when(heartBeatService).discover(providerEthID);

        ClosureRequest closureRequest1 = new ClosureRequest(
                "GIVEN_NAME",
                ClaimOperation.EQ,
                new ValueHolder("Hans"),
                "asd",
                true,
                true
        );


        ClosureRequest closureRequest2 = new ClosureRequest(
                "FAMILY_NAME",
                ClaimOperation.EQ,
                new ValueHolder("Peter"),
                "asd",
                true,
                false
        );

        permissionRequest.setClosureRequests(
            Sets.newHashSet(closureRequest1, closureRequest2)
        );

        permissionRequest.setRequiredClaims(new HashMap<>());
        permissionRequest.setOptionalClaims(new HashMap<>());

        permissionRequestDB.update(permissionRequest);

        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(permissionRequest);

        ResponseEntity<PermissionRequestDTO> result = restTemplate.exchange("/permissions/" + permissionRequestID,
                HttpMethod.PUT,
                new HttpEntity<>(permissionRequestDTO),
                PermissionRequestDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();

        PermissionRequest permissionRequest = permissionRequestDB.findEntity(permissionRequestID).get();
        assertThat(permissionRequest.getClosureRequests()).containsExactlyInAnyOrder(closureRequest1, closureRequest2);
        verify(ebaInterface).approvePermissionContract(any(Account.class), eq(permissionContractAddress), any(PermissionContractContent.class));
    }

    private void assertPPRContent(Map<String, String> approvedClaims, Set<String> initialClaims) {
        approvedClaims.values().stream().forEach(
                base64 -> {
                    assertThat(base64).isNotNull().isNotEmpty();
                    String encoded = new String(Base64.decode(base64));
                    assertThat(encoded).isNotNull().isNotEmpty();
                    try {
                        SignedRequest<ApprovedClaim> signedRequest = objectMapper.readValue(encoded, javaType);
                        assertThat(signer.verifySignature(signedRequest.getPayload(), signedRequest.getSignature().toSignatureData(), signedRequest.getEthID())).isTrue();
                        assertThat(initialClaims).contains(signedRequest.getPayload().getClaimId());
                        assertThat(signedRequest.getPayload().getProviderEthId()).isEqualTo(providerEthID);
                        assertThat(signedRequest.getPayload().getEthID()).isEqualTo(USER_CREDENTIALS.getAddress());
                    } catch (IOException e) {
                        fail("Error occurred", e);
                        throw new UncheckedIOException(e);
                    }
                }
        );
    }
}
