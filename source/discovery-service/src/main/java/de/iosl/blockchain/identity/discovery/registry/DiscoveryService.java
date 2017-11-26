package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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


}
