package de.iosl.blockchain.identity.core.provider.data.repository;

import de.iosl.blockchain.identity.core.provider.data.user.User;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String>{

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<User> findAll();

}
