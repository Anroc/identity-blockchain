package de.iosl.blockchain.identity.core.shared.users;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class UserDBTest extends RestTestSuite {
    private User user;
    private ProviderClaim providerClaim;
    private ProviderClaim providerClaimTwo;

    private final ClaimFactory claimFactory = new ClaimFactory();

    @Before
    public void init() {
        providerClaim = claimFactory.create("1", ClaimType.STRING, "1");
        providerClaimTwo = claimFactory.create("2", ClaimType.BOOLEAN, true);
        Set<ProviderClaim> providerClaimHashSet = Sets.newHashSet(providerClaim);
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
