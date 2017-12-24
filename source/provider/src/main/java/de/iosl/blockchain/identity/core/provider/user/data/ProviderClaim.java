package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProviderClaim extends SharedClaim {

    @Field
    @Getter(onMethod = @__(@Override))
    @Setter
    private String id;

    public ProviderClaim(String id, Date modificationDate, Date creationDate, Provider provider, Payload claimValue) {
        super(modificationDate, creationDate, provider, claimValue);
        this.id = id;
    }

    public ProviderClaim(ClaimDTO claimDTO) {
        this(claimDTO.getId(),
                claimDTO.getModificationDate(),
                claimDTO.getCreationDate(),
                claimDTO.getProvider().toProvider(),
                claimDTO.getClaimValue().toPayload());
    }

}
