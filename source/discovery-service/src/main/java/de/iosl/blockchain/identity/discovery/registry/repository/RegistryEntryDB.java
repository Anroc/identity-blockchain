package de.iosl.blockchain.identity.discovery.registry.repository;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegistryEntryDB extends CouchbaseWrapper<RegistryEntry, String> {

	private final RegistryEntryRepository registryEntryRepository;

	@Autowired
	public RegistryEntryDB(RegistryEntryRepository registryEntryRepository) {
		super(registryEntryRepository);
		this.registryEntryRepository = registryEntryRepository;
	}

	public List<RegistryEntry> findAll() {
		return registryEntryRepository.findAll();
	}
}
