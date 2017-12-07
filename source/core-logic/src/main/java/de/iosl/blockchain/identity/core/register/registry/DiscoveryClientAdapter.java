package de.iosl.blockchain.identity.core.register.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DiscoveryClientAdapter {

    String PROVIDER_PATH = "/provider";

    @RequestLine("GET " + PROVIDER_PATH)
    List<RegistryEntryDTO> getEntries(@QueryMap Map<String, String> queryParam);

    @RequestLine("POST " + PROVIDER_PATH)
    void register(RegistryEntryDTO registryEntry);

    @RequestLine("GET " + PROVIDER_PATH + "/{ethId}")
    Optional<RegistryEntryDTO> getEntry(@Param("ethID") String ethId);

}
