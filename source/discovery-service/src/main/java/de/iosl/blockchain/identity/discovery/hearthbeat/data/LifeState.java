package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import lombok.NonNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public enum LifeState {

    ONLINE, OFFLINE;

    public static LifeState from(@NonNull Date lastSeen) {
        if(Instant.now().minus(30, ChronoUnit.SECONDS).isAfter(lastSeen.toInstant())) {
            return OFFLINE;
        } else {
            return ONLINE;
        }
    }
}
