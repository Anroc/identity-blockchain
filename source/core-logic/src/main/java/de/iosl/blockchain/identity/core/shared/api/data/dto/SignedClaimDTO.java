package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true, value = {"ethID", "id", "modificationDate", "provider", "claimValue"})
@EqualsAndHashCode(callSuper = true)
public class SignedClaimDTO extends BasicEthereumDTO {

    @Field
    @NotBlank
    private String id;
    @Field
    private Date modificationDate;
    @Valid
    @Field
    private ProviderDTO provider;
    @Valid
    @Field
    private PayloadDTO claimValue;

    public SignedClaimDTO(String ethID, String id, Date modificationDate,
            ProviderDTO provider, PayloadDTO claimValue) {
        super(ethID);
        this.id = id;
        this.modificationDate = modificationDate;
        this.provider = provider;
        this.claimValue = claimValue;
    }
}
