package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.db.RegistryEntryDB;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class DiscoveryService {

    @Autowired
    private RegistryEntryDB registryEntryDB;

    public Optional<RegistryEntry> getEntry(@NonNull String ethId) {
        return registryEntryDB.findEntity(ethId);
    }

    public RegistryEntry putEntry(@NonNull RegistryEntry registryEntry) {
        if(registryEntryDB.exist(registryEntry.getId())) {
            registryEntryDB.deleteEntity(registryEntry.getId());
        }
        return registryEntryDB.insert(registryEntry);
    }

    public Collection<RegistryEntry> getEntries() {
        return registryEntryDB.findAll();
    }
}
