package de.iosl.blockchain.identity.discovery.registry.db;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistryEntryRepository
        extends CrudRepository<RegistryEntry, String> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<RegistryEntry> findAll();

}
