package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import de.iosl.blockchain.identity.core.shared.claims.repository.ClaimDB;
import org.assertj.core.util.Maps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;

public class ClaimTest {
    private final Date createdDate = java.sql.Date.valueOf(LocalDate.now());
    private final Date lastModifiedDate = java.sql.Date.valueOf(LocalDate.now());
    private Payload payload = new Payload(Maps.newHashMap("1","1"));
    private Provider provider = new Provider("1","asd","asd");
    private Claim claim;

    @Autowired
    public ClaimDB claimDB;

    //@Before
    public void init(){
        claim = new Claim("1",createdDate,lastModifiedDate,payload,provider);
    }

    //@Test
    public void saveClaimTest(){
        claimDB.saveClaim(claim);
    }

    //@After
    public void clearDB(){
        claimDB.deleteAll(Claim.class);
    }
}
