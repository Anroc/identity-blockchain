package de.iosl.blockchain.identity.core.provider.user.data;

import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.payload.PayloadType;
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
    private Object payload;
    @NotNull
    private PayloadType payloadType;

    public PayloadDTO(@NonNull Payload claimValue) {
        this.payload = claimValue.getPayload();
        this.payloadType = claimValue.getPayloadType();
    }

    public Payload toPayload() {
        return new Payload(getPayload(), getPayloadType());
    }
}
