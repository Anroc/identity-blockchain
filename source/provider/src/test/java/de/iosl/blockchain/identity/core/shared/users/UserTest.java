package de.iosl.blockchain.identity.core.shared.users;

import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.data.repository.UserDB;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class UserTest {
    private User user;
    private Claim claim;
    private Claim claimTwo;
    private Date createdDate = java.sql.Date.valueOf(LocalDate.now());
    private Date lastModifiedDate = java.sql.Date.valueOf(LocalDate.now());

    @Autowired
    public UserDB userDB;
    @Autowired
    public BlockchainIdentityConfig config;

    @Before
    public void init(){
        claim = new Claim("1", lastModifiedDate, createdDate,
                new Provider("1", "1", "1"),
                new Payload("1", Payload.PayloadType.STRING));
        claimTwo = new Claim("2", lastModifiedDate, createdDate,
                new Provider("2", "2", "2"),
                new Payload(true, Payload.PayloadType.BOOLEAN));
        ArrayList<Claim> claimList = new ArrayList<>();
        claimList.add(claim);
        user = new User("1", "1", "1", claimList);
    }

    @Test
    public void saveUser(){
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findEntity(user.getId())).isPresent();
        clearDatabase();
    }

    @Test
    public void addClaimToUser(){
        userDB.updateOrCreateUser(user);
        userDB.addClaimToUser(user.getId(), claimTwo);
        // real nice that findEntity returns id field as null even though it can write it
        claimTwo.setId(null);
        assertThat(userDB.findEntity(user.getId()).get().getClaimList().contains(claimTwo)).isTrue();
        clearDatabase();
    }

    @Test
    public void removeClaimFromUser(){
        userDB.updateOrCreateUser(user);
        userDB.removeClaimFromUser(user.getId(), claim);
        assertThat(userDB.findEntity(user.getId()).get().getClaimList().contains(claim)).isFalse();
        clearDatabase();
    }


    @Test
    public void findAllUsers(){
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findAll().size()).isGreaterThan(0);
        clearDatabase();
    }

    @Test
    public void findUserByPublicKey(){
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findUserByPublicKey("1")).isNotNull();
        clearDatabase();
    }

    @Test
    public void findUserByFindOne(){
        userDB.updateOrCreateUser(user);
        assertThat(userDB.findOne(user.getId())).isPresent();
        clearDatabase();
    }

    @Test
    public void deleteUser(){
        userDB.deleteUser(user.getId());
        assertThat(userDB.findEntity(user.getId())).isNotPresent();
        clearDatabase();
    }

    @Test
    public void clearDatabase(){
        userDB.deleteAll(User.class);
        assertThat(userDB.findAll().size()).isEqualTo(0);
    }
}
