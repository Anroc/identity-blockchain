package de.iosl.blockchain.identity.discovery.registry.repository;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscoveryServiceRepository extends CrudRepository <RegistryEntry,String>{
    @Query("#{#n1ql.delete}")
    List<Object> deleteAllEntries();
}
