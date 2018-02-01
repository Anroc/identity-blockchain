package de.iosl.blockchain.identity.core.provider.permission;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.api.client.APIProviderService;
import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.permission.data.dto.PermissionRequestDTO;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PermissionRequestControllerRestTest extends RestTestSuite {

    private static Credentials PROVIDER_CREDENTIALS;

    private final String userEthID = "0x123";
    private final String providerEthID = PROVIDER_CREDENTIALS.getAddress();
    private final String url = "https://postbank.de:4321";
    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final Set<String> requiredClaims = Sets.newHashSet(claimID_givenName, claimID_familyName);
    private final Set<String> optionalClaims = Sets.newHashSet(claimID_age);
    private final String permissionContractAddress = "0xabc";

    private User user;
    private MultiValueMap<String, String> headers;

    @Autowired
    private KeyChain keyChain;
    @SpyBean
    private APIProviderService apiProviderService;
    @SpyBean
    private PermissionRequestService permissionRequestService;

    @BeforeClass
    public static void setup() throws IOException, CipherException {
        PROVIDER_CREDENTIALS = loadWallet(PROVIDER_FILE, WALLET_PW);
    }

    @Before
    public void init() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.AUTHORIZATION, getAuthentication());

        headers = map;

        user = userFactory.create();
        user.setEthId(userEthID);

        userDB.insert(user);

        keyChain.setAccount(getAccountFromCredentials(PROVIDER_CREDENTIALS));
    }

    @Test
    public void createPermissionRequest_existingUser() {
        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(userEthID, url, requiredClaims, optionalClaims, null);

        doNothing().when(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);
        doReturn(permissionContractAddress).when(apiProviderService).requestUserClaims(any(PermissionRequest.class));

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/permissions",
                HttpMethod.POST,
                new HttpEntity<>(permissionRequestDTO, headers),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        User user = userDB.findEntity(this.user.getId()).get();
        assertThat(user.getPermissionGrands()).hasSize(1);
        assertThat(user.getPermissionGrands().get(0).getPermissionContractAddress()).isEqualTo(permissionContractAddress);
        assertThat(user.getPermissionGrands().get(0).getRequiredClaimGrants())
                .containsOnlyKeys(claimID_givenName, claimID_familyName).doesNotContainValue(true);
        assertThat(user.getPermissionGrands().get(0).getOptionalClaimGrants())
                .containsOnlyKeys(claimID_age).doesNotContainValue(true);
    }

    @Test
    public void createPermissionRequest_nonExisitingUser() {
        final String otherEthID = "0xcafeaffe";
        PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(otherEthID, url, requiredClaims, optionalClaims, null);

        doNothing().when(permissionRequestService).registerPermissionContractListener(permissionContractAddress, userEthID, url);
        doReturn(permissionContractAddress).when(apiProviderService).requestUserClaims(any(PermissionRequest.class));

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/permissions",
                HttpMethod.POST,
                new HttpEntity<>(permissionRequestDTO, headers),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Optional<User> userOptional = userDB.findUserByEthId(otherEthID);
        assertThat(userOptional).isPresent();
        user = userOptional.get();
        assertThat(user.getId()).isNotEmpty();
        assertThat(user.getEthId()).isNotEmpty();
        assertThat(user.getRegisterContractAddress()).isNull();
        assertThat(user.getPermissionGrands()).hasSize(1);
        assertThat(user.getPermissionGrands().get(0).getPermissionContractAddress()).isEqualTo(permissionContractAddress);
        assertThat(user.getPermissionGrands().get(0).getRequiredClaimGrants())
                .containsOnlyKeys(claimID_givenName, claimID_familyName).doesNotContainValue(true);
        assertThat(user.getPermissionGrands().get(0).getOptionalClaimGrants())
                .containsOnlyKeys(claimID_age).doesNotContainValue(true);
    }
}
