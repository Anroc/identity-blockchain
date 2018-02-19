package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignedRequest<T extends BasicEthereumDTO> {

    @Valid
    @NotNull
    @Field
    private T payload;

    @Valid
    @NotNull
    @Field
    private ECSignature signature;

    @JsonIgnore
    public String getEthID() {
        return getPayload().getEthID();
    }
}
