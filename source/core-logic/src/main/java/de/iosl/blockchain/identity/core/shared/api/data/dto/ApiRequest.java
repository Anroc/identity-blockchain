package de.iosl.blockchain.identity.core.shared.api.data.dto;

import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest<T> {

    @Valid
    @NotNull
    private T payload;

    @Valid
    @NotNull
    private ECSignature signature;
}
