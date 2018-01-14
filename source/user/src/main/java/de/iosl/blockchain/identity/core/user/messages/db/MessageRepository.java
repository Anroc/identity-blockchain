package de.iosl.blockchain.identity.core.user.messages.db;

import de.iosl.blockchain.identity.core.user.messages.data.Message;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND seen = $1 ORDER BY creationDate")
    List<Message> findAllBySeen(boolean seen);
}
