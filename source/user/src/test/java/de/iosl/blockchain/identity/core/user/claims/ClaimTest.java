package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.db.UserClaimDB;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ClaimTest {
    private final Date lastModifiedDate = new Date();
    private UserClaim userClaim;

    @Autowired
    private UserClaimDB userClaimDB;

    @Autowired
    private BlockchainIdentityConfig config;

    @Before
    public void init() {
        userClaim = new UserClaim("1", lastModifiedDate,
                new Provider("1", "1"),
                new Payload("1", ClaimType.STRING),
                "0x123");
    }

    @Test
    public void saveClaimTest() {
        userClaimDB.insert(userClaim);
        assertThat(userClaimDB.findEntity(userClaim.getId())).isPresent();
    }

    @Test
    public void saveAndReadDateClaimTest() {
        UserClaim userClaim = new UserClaim(
                "id",
                new Date(),
                new Provider("1", "1"),
                new Payload(LocalDateTime.now(), ClaimType.DATE),
                "0x123"
        );

        assertThat(userClaim.getClaimValue().getPayload()).isInstanceOf(LocalDateTime.class);

        userClaimDB.insert(userClaim);
        UserClaim retrievedClaim = userClaimDB.findEntity("id").get();

        assertThat(userClaim).isEqualTo(retrievedClaim);
        assertThat(userClaim.getClaimValue().getPayload()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void removeClaimTest() {
        userClaimDB.insert(userClaim);
        userClaimDB.delete(userClaim.getId());
        assertThat(userClaimDB.findEntity(userClaim.getId())).isNotPresent();
    }

    @Test
    public void retrieveClaimByEthId() {
        userClaimDB.insert(userClaim);
        assertThat(userClaimDB.findAllByEthID(userClaim.getTargetUserEthID())).hasSize(1);
    }

    @After
    public void clearDB() {
        userClaimDB.deleteAll(UserClaim.class);
    }
}
