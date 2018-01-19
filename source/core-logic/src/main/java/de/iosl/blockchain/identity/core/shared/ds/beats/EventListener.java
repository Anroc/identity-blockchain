package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import lombok.NonNull;

public interface EventListener {

    void trigger(@NonNull Event event, @NonNull EventType eventType);

}
