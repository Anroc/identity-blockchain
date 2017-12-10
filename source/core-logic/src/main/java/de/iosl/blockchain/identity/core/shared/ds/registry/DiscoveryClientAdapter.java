package de.iosl.blockchain.identity.core.shared.ds.registry;

import de.iosl.blockchain.identity.core.shared.ds.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.core.shared.ds.dto.RequestDTO;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DiscoveryClientAdapter {

    String PROVIDER_PATH = "/provider";

    @RequestLine("GET " + PROVIDER_PATH)
    List<RequestDTO<RegistryEntryDTO>> getEntries(@QueryMap Map<String, String> queryParam);

    @RequestLine("POST " + PROVIDER_PATH)
    @Headers("Content-Type: application/json")
    void register(RequestDTO<RegistryEntryDTO> registryEntry);

    @RequestLine("GET " + PROVIDER_PATH + "/{ethId}")
    Optional<RequestDTO<RegistryEntryDTO>> getEntry(@Param("ethID") String ethId);

}
