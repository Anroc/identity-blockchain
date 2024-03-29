package de.iosl.blockchain.identity.core.provider.api;

import com.google.common.collect.Lists;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.*;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.util.List;

import static de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ApiControllerRestTest extends RestTestSuite {

    private static Credentials USER_CREDENTIALS;
    private static Credentials REQUESTING_PROVIDER_CREDENTIALS;
    private static Credentials PROVIDER_CREDENTIALS;

    private User user;

    @Autowired
    private ProviderConfig config;
    @Autowired
    private KeyChain keyChain;

    @BeforeClass
    public static void setup() throws IOException, CipherException {
        PROVIDER_CREDENTIALS = loadWallet(PROVIDER_FILE, WALLET_PW);
        USER_CREDENTIALS = loadWallet(USER_FILE, WALLET_PW);
        REQUESTING_PROVIDER_CREDENTIALS = loadWallet(PROVIDER_FILE_2, WALLET_PW);
    }

    @Before
    public void init() {
        user = userFactory.create();
        user.setEthId(USER_CREDENTIALS.getAddress());
        user.setPublicKey(KeyConverter.from("This ain't no key".getBytes()).toBase64());

        userDB.insert(user);

        keyChain.setAccount(getAccountFromCredentials(PROVIDER_CREDENTIALS));
    }

    @Test
    public void getClaimsTest() {
        ProviderClaim providerClaim = claimFactory.create("SPECIAL_CLAIM_ID");
        user.putClaim(providerClaim);

        userDB.update(user);

        BasicEthereumDTO basicEthereumDTO = new BasicEthereumDTO(USER_CREDENTIALS.getAddress());

        SignedRequest<BasicEthereumDTO> claimRequest = new SignedRequest<>(
                basicEthereumDTO,
                getSignature(basicEthereumDTO, USER_CREDENTIALS)
        );

        ResponseEntity<List<ClaimDTO>> responseEntity = restTemplate.exchange(
                ABSOLUTE_CLAIM_ATH,
                HttpMethod.POST,
                new HttpEntity<>(claimRequest),
                new ParameterizedTypeReference<List<ClaimDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains(new ClaimDTO(providerClaim));
    }

    @Test
    public void infoTest() {
        InfoDTO expected = new InfoDTO(config.getBuildVersion(), config.getApiVersion(), config.getApplicationName());

        ResponseEntity<InfoDTO> responseEntity = restTemplate.getForEntity(
                ABSOLUTE_INFO_PATH,
                InfoDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expected);
    }

    @Test
    public void createPermissionContract() {
        final String claimID = "SPECIAL_CLAIM_ID";
        final String pprEthID = "0x1";
        ProviderClaim providerClaim = claimFactory.create(claimID);
        user.putClaim(providerClaim);
        userDB.update(user);

        List<String> requiredClaim = Lists.newArrayList(claimID);

        doReturn(pprEthID).when(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                any(PermissionContractContent.class)
        );

        doReturn(mock(Beat.class)).when(heartBeatService).createEthIdBeat(user.getEthId(), EventType.NEW_PPR, pprEthID);

        PermissionContractCreationDTO permissionContractCreationDTO = new PermissionContractCreationDTO(
                REQUESTING_PROVIDER_CREDENTIALS.getAddress(),
                requiredClaim,
                Lists.newArrayList(),
                Lists.newArrayList()
        );

        SignedRequest<PermissionContractCreationDTO> signedRequest = new SignedRequest<>(
                permissionContractCreationDTO,
                getSignature(permissionContractCreationDTO, REQUESTING_PROVIDER_CREDENTIALS)
        );

        ResponseEntity<BasicEthereumDTO> responseEntity = restTemplate.exchange(
                getUrl(ABSOLUTE_PPR_PATH, user.getEthId()),
                HttpMethod.POST,
                new HttpEntity<>(signedRequest),
                BasicEthereumDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getEthID()).isEqualTo(pprEthID);
        verify(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                any(PermissionContractContent.class)
        );
    }

    @Test
    public void createPermissionContractWithClosure() {
        AsymmetricCryptEngine<String> cryptEngine = CryptEngine.generate().string().rsa();
        String userPublicKey = new KeyConverter().from(
                cryptEngine.getPublicKey()
        ).toBase64();

        final String claimID = "SPECIAL_CLAIM_ID";
        final String pprEthID = "0x1";
        ProviderClaim providerClaim = claimFactory.create(claimID);
        user.putClaim(providerClaim);
        user.setPublicKey(userPublicKey);
        userDB.update(user);

        doReturn(pprEthID).when(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                any(PermissionContractContent.class)
        );

        doReturn(mock(Beat.class)).when(heartBeatService).createEthIdBeat(user.getEthId(), EventType.NEW_PPR, pprEthID);

        PermissionContractCreationDTO permissionContractCreationDTO = new PermissionContractCreationDTO(
                REQUESTING_PROVIDER_CREDENTIALS.getAddress(),
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList(
                        new ClosureContractRequestDTO(
                                claimID,
                                ClaimOperation.EQ,
                                new ValueHolder("content")
                        )
                )
        );

        SignedRequest<PermissionContractCreationDTO> signedRequest = new SignedRequest<>(
                permissionContractCreationDTO,
                getSignature(permissionContractCreationDTO, REQUESTING_PROVIDER_CREDENTIALS)
        );

        ResponseEntity<BasicEthereumDTO> responseEntity = restTemplate.exchange(
                getUrl(ABSOLUTE_PPR_PATH, user.getEthId()),
                HttpMethod.POST,
                new HttpEntity<>(signedRequest),
                BasicEthereumDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getEthID()).isEqualTo(pprEthID);
        verify(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                any(PermissionContractContent.class)
        );
    }

    @Test
    public void retrieveClaimsByPPR() {
        final String claimID = "SPECIAL_CLAIM_ID";
        final String pprEthID = "0x1";
        ProviderClaim providerClaim = claimFactory.create(claimID);
        user.putClaim(providerClaim);
        userDB.update(user);

        ApprovedClaim approvedClaim = new ApprovedClaim(user.getEthId(), claimID, REQUESTING_PROVIDER_CREDENTIALS.getAddress());
        SignedRequest<ApprovedClaim> signedClaimsRequest = new SignedRequest<>(approvedClaim, getSignature(approvedClaim, USER_CREDENTIALS));

        SignedClaimRequestDTO signedClaimRequestDTO = new SignedClaimRequestDTO(
                REQUESTING_PROVIDER_CREDENTIALS.getAddress(),
                pprEthID,
                Lists.newArrayList(signedClaimsRequest),
                Lists.newArrayList()
        );

        SignedRequest<SignedClaimRequestDTO> signedRequest = new SignedRequest<>(
                signedClaimRequestDTO,
                getSignature(signedClaimRequestDTO, REQUESTING_PROVIDER_CREDENTIALS)
        );

        ResponseEntity<PermissionContractResponse> responseEntity = restTemplate.exchange(
                getUrl(ABSOLUTE_PPR_PATH, user.getEthId()),
                HttpMethod.PUT,
                new HttpEntity<>(signedRequest),
                PermissionContractResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getClaims()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(responseEntity.getBody().getClaims().get(0)).isEqualToIgnoringGivenFields(new ClaimDTO(providerClaim), "signedClosures");
    }



    private String getUrl(String url, String ethID) {
        String resolvedUrl = new String(url);
        return resolvedUrl.replaceAll("\\{" + ETH_ID_PARAM +"\\}", ethID);
    }
}
