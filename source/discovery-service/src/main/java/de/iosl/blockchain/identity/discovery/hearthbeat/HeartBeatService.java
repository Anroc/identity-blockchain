package de.iosl.blockchain.identity.discovery.hearthbeat;

import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import de.iosl.blockchain.identity.discovery.hearthbeat.db.BeatDB;
import de.iosl.blockchain.identity.discovery.registry.db.RegistryEntryDB;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import de.iosl.blockchain.identity.lib.dto.beats.HeartBeatRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HeartBeatService {

    @Autowired
    private RegistryEntryDB registryEntryDB;
    @Autowired
    private BeatDB beatDB;

    public List<Beat> heartBeat(@NonNull String ethID, long from, long to) {
        registryEntryDB.updateLastSeen(ethID, new Date());
        long counter = beatDB.getCounter(ethID);

        if (from < counter) {
            if( to >= Long.MAX_VALUE) {
                to = counter;
            }

            return beatDB.findBeatsByEthIDAndCounterRange(ethID, from, to);
        } else {
            return new ArrayList<>();
        }
    }

    public Beat createBeat(@NonNull RequestDTO<HeartBeatRequest> messageRequest, @NonNull String ethID) {
        long counter = beatDB.getCounter(ethID); // init on non exist
        beatDB.incrementCounter(ethID);

        Beat message = new Beat(
                counter,
                ethID,
                messageRequest.getPayload().getEthID(),
                messageRequest.getPayload().getSubject(),
                messageRequest.getPayload().getSubjectType(),
                messageRequest.getPayload().getEventType(),
                messageRequest.getSignature()
        );
        beatDB.insert(message);
        return message;
    }
}
