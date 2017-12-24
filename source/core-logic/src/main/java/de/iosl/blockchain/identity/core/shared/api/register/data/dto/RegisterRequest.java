package de.iosl.blockchain.identity.core.shared.api.register.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends ApiRequest<RegisterRequestPayloadDTO> {

    public RegisterRequest(
            RegisterRequestPayloadDTO payload,
            ECSignature ecSignature,
            ECSignature governmentSignature) {
        super(payload, ecSignature);
        this.governmentSignature = governmentSignature;
    }

    /**
     * Signature via ethereum Address
     */
    @NotNull
    @Valid
    private ECSignature governmentSignature;
}
