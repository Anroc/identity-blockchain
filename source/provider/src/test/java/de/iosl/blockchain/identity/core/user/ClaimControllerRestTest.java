package de.iosl.blockchain.identity.core.user;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.factories.UserFactory;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.provider.user.data.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimControllerRestTest extends RestTestSuite {

    UserFactory userFactory = UserFactory.instance();

    private User user;

    @Before
    public void setup() {
        user = userFactory.create();
    }

    @Test
    public void createUser() {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setEthId(null);
        userDTO.setPublicKey(null);
        userDTO.setId(null);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.AUTHORIZATION, getAuthentication());

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/user", HttpMethod.POST,
                        new HttpEntity<>(userDTO, map), UserDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        UserDTO responseDTO = responseEntity.getBody();

        assertThat(responseDTO.getId()).isNotNull().isNotEmpty();
        assertThat(userDB.findEntity(responseDTO.getId())).isPresent();
        assertThat(userDB.findOne(responseDTO.getId()).get()).isEqualToIgnoringGivenFields(
                userDTO, "id"
        );
    }
}
