package de.iosl.blockchain.identity.core.shared.message.db;

import de.iosl.blockchain.identity.core.shared.message.data.Message;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY creationDate")
    List<Message> findAll();
}
