package de.iosl.blockchain.identity.core.provider.api;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.PermissionContractCreationDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.SignedClaimRequestDTO;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import org.assertj.core.util.Lists;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProviderAPIRestTest extends RestTestSuite {

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
                ProviderAPIConstances.ABSOLUTE_CLAIM_ATH,
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
                ProviderAPIConstances.ABSOLUTE_INFO_PATH,
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

        Set<String> requiredClaim = Sets.newHashSet(claimID);

        doReturn(pprEthID).when(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                eq(REQUESTING_PROVIDER_CREDENTIALS.getAddress()),
                eq(requiredClaim),
                eq(Sets.newHashSet())
        );

        doReturn(mock(Beat.class)).when(heartBeatService).createEthIdBeat(user.getEthId(), EventType.NEW_PPR, pprEthID);

        PermissionContractCreationDTO permissionContractCreationDTO = new PermissionContractCreationDTO(
                REQUESTING_PROVIDER_CREDENTIALS.getAddress(),
                requiredClaim,
                Sets.newHashSet()
        );

        SignedRequest<PermissionContractCreationDTO> signedRequest = new SignedRequest<>(
                permissionContractCreationDTO,
                getSignature(permissionContractCreationDTO, REQUESTING_PROVIDER_CREDENTIALS)
        );

        ResponseEntity<BasicEthereumDTO> responseEntity = restTemplate.exchange(
                getUrl(ProviderAPIConstances.ABSOLUTE_PPR_PATH, user.getEthId()),
                HttpMethod.POST,
                new HttpEntity<>(signedRequest),
                BasicEthereumDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getEthID()).isEqualTo(pprEthID);
        verify(ebaInterface).deployPermissionContract(
                any(Account.class),
                eq(user.getEthId()),
                eq(REQUESTING_PROVIDER_CREDENTIALS.getAddress()),
                eq(requiredClaim),
                eq(Sets.newHashSet())
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
                Lists.newArrayList(signedClaimsRequest)
        );

        SignedRequest<SignedClaimRequestDTO> signedRequest = new SignedRequest<>(
                signedClaimRequestDTO,
                getSignature(signedClaimRequestDTO, REQUESTING_PROVIDER_CREDENTIALS)
        );

        ResponseEntity<List<ClaimDTO>> responseEntity = restTemplate.exchange(
                getUrl(ProviderAPIConstances.ABSOLUTE_PPR_PATH, user.getEthId()),
                HttpMethod.PUT,
                new HttpEntity<>(signedRequest),
                new ParameterizedTypeReference<List<ClaimDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(responseEntity.getBody().get(0)).isEqualTo(new ClaimDTO(providerClaim));
    }



    private String getUrl(String url, String ethID) {
        String resolvedUrl = new String(url);
        return resolvedUrl.replaceAll("\\{ethID\\}", ethID);
    }
}
