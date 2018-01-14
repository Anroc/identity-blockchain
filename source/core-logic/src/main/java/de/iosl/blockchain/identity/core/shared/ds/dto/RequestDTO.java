package de.iosl.blockchain.identity.core.shared.ds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO<T extends Payload> {

    @Valid
    @NotNull
    private T payload;

    @Valid
    @NotNull
    private ECSignature signature;
}
