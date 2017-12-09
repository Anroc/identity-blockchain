package de.iosl.blockchain.identity.core.shared.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryEntryDTO {

    @Valid
    @NotNull
    private Payload payload;

    @Valid
    @NotNull
    private ECSignature signature;
}
