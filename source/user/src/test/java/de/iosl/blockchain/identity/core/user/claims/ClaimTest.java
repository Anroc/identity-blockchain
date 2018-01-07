package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.payload.PayloadType;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.repository.UserClaimDB;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
                new Payload("1", PayloadType.STRING),
                "0x123");
    }

    @Test
    public void saveClaimTest() {
        userClaimDB.save(userClaim);
        assertThat(userClaimDB.findEntity(userClaim.getId())).isPresent();
    }

    @Test
    public void removeClaimTest() {
        userClaimDB.save(userClaim);
        userClaimDB.delete(userClaim.getId());
        assertThat(userClaimDB.findEntity(userClaim.getId())).isNotPresent();
    }

    @Test
    public void retrieveClaimByEthId() {
        userClaimDB.save(userClaim);
        assertThat(userClaimDB.findAllByEthID(userClaim.getTargetUserEthID())).hasSize(1);
    }

    @After
    public void clearDB() {
        userClaimDB.deleteAll(UserClaim.class);
    }
}
