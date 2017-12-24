package de.iosl.blockchain.identity.core.provider.user.db;

import de.iosl.blockchain.identity.core.provider.user.data.User;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String>{

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<User> findAll();

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND ethId = $1 LIMIT 1")
    Optional<User> findByEthID(String ethId);

}
