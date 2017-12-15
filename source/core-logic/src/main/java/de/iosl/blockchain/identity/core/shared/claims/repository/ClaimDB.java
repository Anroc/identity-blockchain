package de.iosl.blockchain.identity.core.shared.claims.repository;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Bool;

import java.util.List;
import java.util.Optional;

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

    public void saveClaim(Claim claim){
        claimRepository.save(claim);
    }

    public Claim findOne(String id){
        return claimRepository.findOne(id);
    }

    public void deleteClaim(String id){
        claimRepository.delete(id);
    }
}
