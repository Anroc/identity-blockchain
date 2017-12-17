package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.core.shared.ds.beats.data.Beat;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface HeartBeatAdapter {

    String PROVIDER_PATH = "/heartbeat";

    @RequestLine("GET " + PROVIDER_PATH + "/{ethID}?from={from}&to={to}")
    List<Beat> beat(@Param("ethID") String ethId, @Param("from") long from, @Param("to") long to);
}
