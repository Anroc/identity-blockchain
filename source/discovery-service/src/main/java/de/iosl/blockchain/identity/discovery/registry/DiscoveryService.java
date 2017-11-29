package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DiscoveryService {

	private Map<String, RegistryEntry> registry = new HashMap<>();

	public Optional<RegistryEntry> getEntry(@NonNull String ethId) {
		return Optional.ofNullable(registry.get(ethId));
	}

	public boolean putEntry(@NonNull RegistryEntry registryEntry) {
		boolean ret = registry.containsKey(registryEntry.getPayload().getEthID());
		registry.put(registryEntry.getPayload().getEthID(), registryEntry);
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
	}

}
