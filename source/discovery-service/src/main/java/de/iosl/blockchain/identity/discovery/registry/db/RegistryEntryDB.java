package de.iosl.blockchain.identity.discovery.registry.db;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RegistryEntryDB extends CouchbaseWrapper<RegistryEntry, String> {

    private final RegistryEntryRepository registryEntryRepository;
    private final Bucket bucket;

    @Autowired
    public RegistryEntryDB(RegistryEntryRepository registryEntryRepository, Bucket bucket) {
        super(registryEntryRepository);
        this.registryEntryRepository = registryEntryRepository;
        this.bucket = bucket;
    }

    public List<RegistryEntry> findAll() {
        return registryEntryRepository.findAll();
    }

    public void updateLastSeen(@NonNull String ethID, @NonNull Date date) {
        try {
            bucket.mutateIn(ethID).replace("creationDate", date).execute();
        } catch (DocumentDoesNotExistException e) {
            throw new ServiceException(e, HttpStatus.NOT_FOUND);
        }
    }
}
