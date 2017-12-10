package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import de.iosl.blockchain.identity.discovery.data.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageRequest extends Payload{

    @NotBlank
    private String endpoint;

    public MessageRequest(String ethID, String endpoint) {
        super(ethID);
        this.endpoint = endpoint;
    }
}
