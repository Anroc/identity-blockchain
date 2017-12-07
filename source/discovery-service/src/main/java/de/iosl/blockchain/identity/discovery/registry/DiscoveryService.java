package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.repository.DiscoveryServiceRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class DiscoveryService {
	@Autowired
	private DiscoveryServiceRepository discoveryServiceRepository;

	private Map<String, RegistryEntry> registry = new HashMap<>();

	public Optional<RegistryEntry> getEntry(@NonNull String ethId) {
		return Optional.of(discoveryServiceRepository.findOne(ethId));
	}

	public boolean putEntry(@NonNull RegistryEntry registryEntry) {
		boolean ret = registry.containsKey(registryEntry.getPayload().getEthId());
		registryEntry.setId(registryEntry.getPayload().getEthId());
		discoveryServiceRepository.save(registryEntry);
		return ret;
	}

	public boolean exists(@NonNull String ethID) {
		return registry.containsKey(ethID);
	}

	public Collection<RegistryEntry> getEntries() {
		return registry.values();
	}

	public void dropEntries() {
		registry = new HashMap<>();
		discoveryServiceRepository.deleteAllEntries();
	}

}
