package de.iosl.blockchain.identity.core.shared.claims.repository;

import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, String>{

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<Claim> findAll();

}
