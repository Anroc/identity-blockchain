package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProviderClaim extends SharedClaim {

    @Field
    @Getter(onMethod = @__(@Override))
    @Setter
    private String id;

    public ProviderClaim(@NonNull ClaimDTO claimDTO) {
        super(claimDTO);
    }

    public static ProviderClaim init(@NonNull String id) {
        ProviderClaim providerClaim = new ProviderClaim();
        providerClaim.setId(id);
        return providerClaim;
    }
}
