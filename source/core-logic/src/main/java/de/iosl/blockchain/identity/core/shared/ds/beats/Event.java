package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.core.shared.ds.beats.data.Beat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String ethID;
    private String endpoint;
    private Date createdAt;

    public Event(@NonNull Beat beat) {
        this.ethID = beat.getPayload().getEthID();
        this.endpoint = beat.getPayload().getEndpoint();
        this.createdAt = beat.getCreatedAt();
    }
}