package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import de.iosl.blockchain.identity.core.shared.claims.repository.ClaimDB;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import org.assertj.core.util.Maps;
import org.hibernate.validator.internal.xml.PayloadType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ClaimTest {
    private final Date createdDate = java.sql.Date.valueOf(LocalDate.now());
    private final Date lastModifiedDate = java.sql.Date.valueOf(LocalDate.now());
    private Claim claim;

    @Autowired
    private ClaimDB claimDB;

    @Autowired
    private BlockchainIdentityConfig config;

    @Before
    private void init() {
        claim = new Claim("1", lastModifiedDate, createdDate, new Provider("1", "1", "1"), new Payload("1", Payload.PayloadType.STRING));
    }

    @Test
    private void saveClaimTest() {
        claimDB.saveClaim(claim);
        assertThat(claimDB.findEntity(claim.getId())).isPresent();
    }

    @Test
    private void removeClaimTest() {
        claimDB.deleteClaim("1");
    }

    @After
    private void clearDB() {
        claimDB.deleteAll(Claim.class);
    }
}
