package de.iosl.blockchain.identity.core.user.claims.repository;

import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClaimRepository extends CrudRepository<UserClaim, String> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<UserClaim> findAll();

}
