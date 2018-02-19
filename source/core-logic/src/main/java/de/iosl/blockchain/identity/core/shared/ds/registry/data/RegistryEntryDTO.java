package de.iosl.blockchain.identity.core.shared.ds.registry.data;

import de.iosl.blockchain.identity.lib.dto.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegistryEntryDTO extends Payload {

    @NotBlank
    private String publicKey;
    @NotBlank
    private String domainName;
    @Min(0)
    private int port;

}