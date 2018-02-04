package de.iosl.blockchain.identity.core.shared.claims.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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

    private List<SignedRequest<Closure>> signedClosures;

    public abstract String getId();

    public abstract void setId(String id);

    public SharedClaim(ClaimDTO claimDTO) {
        this(claimDTO.getModificationDate(),
                claimDTO.getProvider().toProvider(),
                claimDTO.getClaimValue().toPayload(),
                claimDTO.getSignedClosures()
        );
        setId(claimDTO.getId());
    }

}
