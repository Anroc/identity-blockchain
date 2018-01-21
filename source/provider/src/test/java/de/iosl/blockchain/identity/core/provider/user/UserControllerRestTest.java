package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.user.data.dto.UserDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.register.data.dto.RegisterRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.Beat;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.EventType;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class UserControllerRestTest extends RestTestSuite {

    private User user;
    private MultiValueMap<String, String> headers;

    @Before
    public void setup() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.AUTHORIZATION, getAuthentication());

        this.headers = map;

        user = userFactory.create();
        userDB.insert(user);
    }

    @Test
    public void create() {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setEthId(null);
        userDTO.setPublicKey(null);
        userDTO.setId(null);


        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/users", HttpMethod.POST,
                        new HttpEntity<>(userDTO, headers), UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        UserDTO responseDTO = responseEntity.getBody();

        assertThat(responseDTO.getId()).isNotNull().isNotEmpty();
        assertThat(userDB.findEntity(responseDTO.getId())).isPresent();
    }

    @Test
    public void get() {
        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/users/" + user.getId(), HttpMethod.GET,
                        new HttpEntity<>(headers), UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void getAll() {
        User user1 = userFactory.create();
        User user2 = userFactory.create();
        User user3 = userFactory.create();

        userDB.insert(user1);
        userDB.insert(user2);
        userDB.insert(user3);

        ResponseEntity<List<UserDTO>> responseEntity = restTemplate
                .exchange("/users/", HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<UserDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        List<String> userIds = responseEntity.getBody()
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    public void deleteUser() {
        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/users/" + user.getId(), HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        assertThat(userDB.exist(user.getId())).isFalse();
    }

    @Test
    public void updateUser() {
        final String ethId = "0x123123";
        final String publicKey = "9ljasdu812938123==";

        user.setEthId(ethId);
        user.setPublicKey(publicKey);
        UserDTO request = new UserDTO(user);

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/users/" + user.getId(), HttpMethod.PUT,
                        new HttpEntity<>(request, headers),
                        UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        User userFromDB = userDB.findEntity(user.getId()).get();
        assertThat(userFromDB.getEthId()).isEqualTo(ethId);
        assertThat(userFromDB.getPublicKey()).isEqualTo(publicKey);
        assertThat(responseEntity.getBody().getEthId()).isEqualTo(ethId);
        assertThat(responseEntity.getBody().getPublicKey()).isEqualTo(publicKey);
    }

    @Test
    public void updateClaim() {
        final ProviderClaim claim = claimFactory.create("new_Claim_id");

        ClaimDTO claimDTO = new ClaimDTO(claim);

        ResponseEntity<ClaimDTO> responseEntity = restTemplate
                .exchange("/users/" + user.getId() + "/claim/", HttpMethod.POST,
                        new HttpEntity<>(claimDTO, headers),
                        ClaimDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Optional<ProviderClaim> claimFromDB = userDB.findEntity(user.getId()).get().findClaim(claimDTO.getId());
        assertThat(claimFromDB).isPresent();
        assertThat(claimFromDB.get()).isEqualToIgnoringGivenFields(claim, "modificationDate");
    }

    @Test
    public void removeClaim() {
        final String claimId = user.getClaims().stream().findFirst().map(SharedClaim::getId).get();

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/users/" + user.getId() + "/claim/" + claimId, HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        Optional<ProviderClaim> claimFromDB = userDB.findEntity(user.getId()).get().findClaim(claimId);
        assertThat(claimFromDB).isNotPresent();
    }

    @Test
    public void registerCredentialsTest() throws IOException, CipherException {
        Credentials userCredentials = loadWallet(USER_FILE, WALLET_PW);
        Credentials stateCredentials = loadWallet(STATE_FILE, WALLET_PW);

        Beat beat = new Beat();
        doReturn(beat).when(heartBeatService).createBeat(eq(userCredentials.getAddress()), eq(EventType.NEW_CLAIMS));
        doNothing().when(ebaInterface).setRegisterApproval(any(Account.class), eq("0x123"), eq(true));

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                userCredentials.getAddress(),
                KeyConverter.from("This ain't no public key".getBytes()).toBase64(),
                "0x123"
        );

        SignedRequest<RegisterRequestDTO> registerRequest = new SignedRequest<>(
                registerRequestDTO,
                getSignature(registerRequestDTO, stateCredentials)
        );

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/users/" + user.getId() + "/register",
                HttpMethod.POST,
                new HttpEntity<>(registerRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        user = userDB.findOne(user.getId()).get();

        assertThat(user.getEthId()).isEqualTo(registerRequest.getPayload().getEthID());
        assertThat(user.getPublicKey()).isEqualTo(registerRequest.getPayload().getPublicKey());
        assertThat(user.getRegisterContractAddress()).isEqualTo(registerRequest.getPayload().getRegisterContractAddress());
        assertThat(userDB.findUserByEthId(user.getEthId())).isPresent();
        verify(heartBeatService, times(1)).createBeat(eq(userCredentials.getAddress()), eq(EventType.NEW_CLAIMS));
    }

    @Test
    public void authorizationTest() {
        ResponseEntity<?> responseEntity = restTemplate
                .exchange("/users/" + user.getId(), HttpMethod.GET,
                        HttpEntity.EMPTY, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
    }

}
