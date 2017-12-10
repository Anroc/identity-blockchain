package de.iosl.blockchain.identity.discovery.hearthbeat.db;

import com.couchbase.client.java.document.json.JsonArray;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Message;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

    @Query("#{#n1ql.selectEntity} USE KEYS $1")
    List<Message> findByIds(JsonArray counterValues);
}
