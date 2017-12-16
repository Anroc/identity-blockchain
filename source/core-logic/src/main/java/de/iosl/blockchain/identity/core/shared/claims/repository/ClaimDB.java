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

    @Autowired
    public ClaimDB(ClaimRepository claimRepository, Bucket bucket) {
        super(claimRepository);
        this.claimRepository = claimRepository;
    }

    public List<Claim> findAll() {
        return claimRepository.findAll();
    }

    public void save(Claim claim){
        claimRepository.save(claim);
    }

    public void delete(String id){
        claimRepository.delete(id);
    }
}
