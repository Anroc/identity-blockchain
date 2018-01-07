package de.iosl.blockchain.identity.core.shared.ds.beats.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.ds.dto.Payload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder(alphabetic=true)
public class HeartBeatRequest extends Payload {

    @NotBlank
    private String url;

    @NotNull
    private EventType eventType;

    public HeartBeatRequest(String ethID, String url, EventType eventType) {
        super(ethID);
        this.url = url;
        this.eventType = eventType;
    }
}
