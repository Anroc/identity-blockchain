package de.iosl.blockchain.identity.core.shared.claims.repository;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClaimDB extends CouchbaseWrapper<Claim, String>{
    private final ClaimRepository claimRepository;
    private final Bucket bucket;

    @Autowired
    public ClaimDB(ClaimRepository claimRepository, Bucket bucket) {
        super(claimRepository);
        this.claimRepository = claimRepository;
        this.bucket = bucket;
    }

    public List<Claim> findAll() {
        return claimRepository.findAll();
    }
}
