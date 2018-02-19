package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import com.google.common.collect.Lists;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
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

    public ProviderClaim(String id, Date modificationDate, Provider provider, Payload claimValue) {
        super(modificationDate, provider, claimValue, Lists.newArrayList());
        this.id = id;
    }

    public ProviderClaim(ClaimDTO claimDTO) {
        super(claimDTO);
    }
}
