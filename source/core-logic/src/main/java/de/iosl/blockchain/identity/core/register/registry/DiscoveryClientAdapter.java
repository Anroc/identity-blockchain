package de.iosl.blockchain.identity.core.register.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DiscoveryClientAdapter {

	String PROVIDER_PATH = "/provider";

	@RequestLine("GET " + PROVIDER_PATH)
	List<RegistryEntry> getEntries(@QueryMap Map<String, String> queryParam);

	@RequestLine("POST " + PROVIDER_PATH)
	void register(RegistryEntry registryEntry);

	@RequestLine("GET " + PROVIDER_PATH + "/{ethId}")
	Optional<RegistryEntry> getEntry(@Param("ethID") String ethId);

}
