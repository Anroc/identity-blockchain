package de.iosl.blockchain.identity.core.user.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserClaim extends SharedClaim {

    @Id
    @Field
    @Getter(onMethod = @__(@Override))
    @Setter
    private String id;

    public UserClaim(String id, Date modificationDate, Provider provider, Payload claimValue) {
        super(modificationDate, provider, claimValue);
        this.id = id;
    }

    public UserClaim(ClaimDTO claimDTO) {
        super(claimDTO);
    }
}
