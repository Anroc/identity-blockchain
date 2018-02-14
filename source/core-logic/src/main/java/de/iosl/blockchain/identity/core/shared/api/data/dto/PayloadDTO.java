package de.iosl.blockchain.identity.core.shared.api.data.dto;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDTO {

    @NotNull
    private ValueHolder payload;
    @NotNull
    private ClaimType payloadType;

    public PayloadDTO(@NonNull Payload claimValue) {
        this.payload = claimValue.getPayload();
        this.payloadType = claimValue.getPayloadType();
    }

    public Payload toPayload() {
        return new Payload(getPayload(), getPayloadType());
    }
}
