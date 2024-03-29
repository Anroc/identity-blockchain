package de.iosl.blockchain.identity.discovery.hearthbeat.db;

import com.couchbase.client.java.document.json.JsonArray;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeatRepository extends CrudRepository<Beat, String> {

    @Query("#{#n1ql.selectEntity} USE KEYS $1 WHERE #{#n1ql.filter}")
    List<Beat> findByIds(JsonArray counterValues);
}
