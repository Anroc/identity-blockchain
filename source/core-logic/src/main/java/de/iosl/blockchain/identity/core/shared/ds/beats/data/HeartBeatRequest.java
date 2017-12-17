package de.iosl.blockchain.identity.core.shared.ds.beats.data;

import de.iosl.blockchain.identity.core.shared.ds.dto.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeartBeatRequest extends Payload {

    @NotBlank
    private String endpoint;

    public HeartBeatRequest(String ethID, String endpoint) {
        super(ethID);
        this.endpoint = endpoint;
    }
}
