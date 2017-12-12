package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import de.iosl.blockchain.identity.core.shared.claims.repository.ClaimDB;
import org.apache.tomcat.jni.Local;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClaimTest {
    private final String id = "1";
    private final Date createdDate = java.sql.Date.valueOf(LocalDate.now());
    private final Date lastModifiedDate = java.sql.Date.valueOf(LocalDate.now());
    private final Map<String, String> content = Maps.newHashMap("1","1");
    private final String ethID = "1";
    private final String providerName = "asd";
    private final String providerPublicKey = "asd";
    private Payload payload;
    private Provider provider;
    private Claim claim;

    @Autowired
    public ClaimDB claimDB;

    @Before
    public void init(){
        payload = new Payload(content);
        provider = new Provider(ethID, providerName, providerPublicKey);
        claim = new Claim(id,createdDate,lastModifiedDate,payload,provider);
    }

    @Test
    public void saveClaimTest(){
        claimDB.saveClaim(claim);
    }
}
