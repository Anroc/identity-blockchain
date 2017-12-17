package de.iosl.blockchain.identity.core.user.claims.repository;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserClaimDB extends CouchbaseWrapper<UserClaim, String> {
    private final UserClaimRepository userClaimRepository;
    private final Bucket bucket;

    @Autowired
    public UserClaimDB(UserClaimRepository userClaimRepository, Bucket bucket) {
        super(userClaimRepository);
        this.userClaimRepository = userClaimRepository;
        this.bucket = bucket;
    }

    public List<UserClaim> findAll() {
        return userClaimRepository.findAll();
    }

    public void save(UserClaim claim){
        userClaimRepository.save(claim);
    }

    public void delete(String id){
        userClaimRepository.delete(id);
    }
}
