package de.iosl.blockchain.identity.core.shared.users;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class UserDBTest extends RestTestSuite {
    private User user;
    private ProviderClaim providerClaim;
    private ProviderClaim providerClaimTwo;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date lastModifiedDate = new Date();

    @Before
    public void init() {
        providerClaim = new ProviderClaim("1", lastModifiedDate,
                new Provider("1", "1"),
                new Payload("1", ClaimType.STRING));
        providerClaimTwo = new ProviderClaim("2", lastModifiedDate,
                new Provider("2", "2"),
                new Payload(true, ClaimType.BOOLEAN));
        Set<ProviderClaim> providerClaimHashSet = new HashSet<>();
        providerClaimHashSet.add(providerClaim);
        user = new User("1", "1", "1", null, new ArrayList<>(), providerClaimHashSet);
    }

    @Test
    public void saveUser() {
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findEntity(user.getId())).isPresent();
    }

    @Test
    public void addClaimToUser() {
        userDB.updateOrCreateUser(user);
        userDB.addClaimToUser(user.getId(), providerClaimTwo);
        Optional<User> userOptional = userDB.findEntity(user.getId());
        assertThat(userOptional).isPresent();
        user = userOptional.get();
        assertThat(user.getClaims().contains(providerClaimTwo)).isTrue();
    }

    @Test
    public void removeClaimFromUser() {
        userDB.updateOrCreateUser(user);
        userDB.removeClaimFromUser(user.getId(), providerClaim);
        Optional<User> userOptional = userDB.findEntity(user.getId());
        assertThat(userOptional).isPresent();
        user = userOptional.get();
        assertThat(user.getClaims().contains(providerClaim)).isFalse();
    }


    @Test
    public void findAllUsers() {
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findAll().size()).isGreaterThan(0);
    }

    @Test
    public void findUserByPublicKey() {
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findUserByEthId("1")).isNotNull();
    }

    @Test
    public void findUserByFindOne() {
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findEntity(user.getId())).isPresent();
    }

    @Test
    public void deleteUser() {
        userDB.deleteUser(user.getId());
        assertThat(userDB.findEntity(user.getId())).isNotPresent();
    }
}
