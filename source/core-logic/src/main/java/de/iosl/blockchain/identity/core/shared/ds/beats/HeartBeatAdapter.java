package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.lib.dto.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.HeartBeatRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface HeartBeatAdapter {

    String HEARTBEAT_PATH = "/heartbeat";
    String REGISTRY_PATH = "/provider";

    @RequestLine("GET " + HEARTBEAT_PATH + "/{ethID}?from={from}&to={to}")
    List<Beat> beat(@Param("ethID") String ethId, @Param("from") long from, @Param("to") long to);

    @Headers("Content-Type: application/json")
    @RequestLine("POST " + HEARTBEAT_PATH + "/{ethID}")
    Beat createBeat(@Param("ethID") String ethId, RequestDTO<HeartBeatRequest> heartBeatRequestRequestDTO);

    @RequestLine("GET " + REGISTRY_PATH + "/{ethID}")
    RequestDTO<RegistryEntryDTO> discover(@Param("ethID") String ethID);
}
