package de.iosl.blockchain.identity.discovery.registry.data;

import de.iosl.blockchain.identity.discovery.data.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryEntryDTO extends Payload {

    @NotBlank
    private String publicKey;
    @NotBlank
    private String domainName;
    @Min(0)
    private int port;

    public RegistryEntryDTO(String ethID, String publicKey,
            String domainName, int port) {
        super(ethID);
        this.publicKey = publicKey;
        this.domainName = domainName;
        this.port = port;
    }
}
