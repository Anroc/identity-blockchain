package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import feign.*;

import java.util.List;
import java.util.Map;

public interface DiscoveryClientAdapter {

	String PROVIDER_PATH = "/provider";

	@RequestLine("GET " + PROVIDER_PATH)
	List<RegistryEntry> getEntries(@QueryMap Map<String, String> queryParam);

	@RequestLine("POST " + PROVIDER_PATH)
	void register(RegistryEntry registryEntry);

}
