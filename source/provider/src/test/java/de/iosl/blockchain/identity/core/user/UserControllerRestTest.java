package de.iosl.blockchain.identity.core.user;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.factories.UserFactory;
import de.iosl.blockchain.identity.core.provider.data.claim.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.provider.user.data.ClaimDTO;
import de.iosl.blockchain.identity.core.provider.user.data.UserDTO;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerRestTest extends RestTestSuite {

    UserFactory userFactory = UserFactory.instance();
    ClaimFactory claimFactory = ClaimFactory.instance();

    private User user;
    private MultiValueMap<String, String> headers;

    @Before
    public void setup() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.AUTHORIZATION, getAuthentication());

        this.headers = map;

        user = userFactory.create();
    }

    @Test
    public void create() {
        createUser();
    }

    @Test
    public void get() {
        User user = createUser();

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/user/" + user.getId(), HttpMethod.GET,
                        new HttpEntity<>(headers), UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void getAll() {
        User user1 = createUser(userFactory.create());
        User user2 = createUser(userFactory.create());
        User user3 = createUser(userFactory.create());

        ResponseEntity<List<UserDTO>> responseEntity = restTemplate
                .exchange("/user/", HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<UserDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        List<String> userIds = responseEntity.getBody()
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        assertThat(userIds).containsExactlyInAnyOrder(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    public void deleteUser() {
        User user = createUser();

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/user/" + user.getId(), HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        assertThat(userDB.exist(user.getId())).isFalse();
    }

    @Test
    public void updateUser() {
        User user = createUser();

        final String ethId = "0x123123";
        final String publicKey = "9ljasdu812938123==";

        user.setEthId(ethId);
        user.setPublicKey(publicKey);
        UserDTO request = new UserDTO(user);

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/user/" + user.getId(), HttpMethod.PUT,
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
        User user = createUser();

        final ProviderClaim claim = claimFactory.create("new_Claim_id");

        ClaimDTO claimDTO = new ClaimDTO(claim);

        ResponseEntity<ClaimDTO> responseEntity = restTemplate
                .exchange("/user/" + user.getId() + "/claim/", HttpMethod.POST,
                        new HttpEntity<>(claimDTO, headers),
                        ClaimDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Optional<SharedClaim> claimFromDB = userDB.findEntity(user.getId()).get().findClaim(claimDTO.getId());
        assertThat(claimFromDB).isPresent();
        assertThat(claimFromDB.get()).isEqualTo(claim);
    }

    @Test
    public void removeClaim() {
        User user = createUser();

        final String claimId = user.getClaims().stream().findFirst().map(SharedClaim::getId).get();

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/user/" + user.getId() + "/claim/" + claimId, HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        Optional<SharedClaim> claimFromDB = userDB.findEntity(user.getId()).get().findClaim(claimId);
        assertThat(claimFromDB).isNotPresent();
    }

    private User createUser() {
        return createUser(user);
    }

    private User createUser(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setEthId(null);
        userDTO.setPublicKey(null);
        userDTO.setId(null);


        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/user", HttpMethod.POST,
                        new HttpEntity<>(userDTO, headers), UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        UserDTO responseDTO = responseEntity.getBody();

        assertThat(responseDTO.getId()).isNotNull().isNotEmpty();
        assertThat(userDB.findEntity(responseDTO.getId())).isPresent();
        return userDB.findEntity(responseDTO.getId()).get();
    }

}
