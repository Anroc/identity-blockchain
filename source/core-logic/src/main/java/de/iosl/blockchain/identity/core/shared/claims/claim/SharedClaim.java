package de.iosl.blockchain.identity.core.shared.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public abstract class SharedClaim {

    @NotNull
    @Field
    private Date modificationDate;

    @NotNull
    @Valid
    private Provider provider;

    @NotNull
    @Valid
    private Payload claimValue;

    public abstract String getId();

    public abstract void setId(String id);

    public SharedClaim(ClaimDTO claimDTO) {
        this(claimDTO.getModificationDate(),
                claimDTO.getProvider().toProvider(),
                claimDTO.getClaimValue().toPayload());
        setId(claimDTO.getId());
    }

}
