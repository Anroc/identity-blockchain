package de.iosl.blockchain.identity.core.user;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.factories.UserFactory;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.provider.user.data.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimControllerRestTest extends RestTestSuite {

    UserFactory userFactory = UserFactory.instance();

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
