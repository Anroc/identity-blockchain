package de.iosl.blockchain.identity.core.user.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserClaim extends SharedClaim {

    @Id
    @Field
    @Getter(onMethod = @__(@Override))
    @Setter
    private String id;

    @Field
    @Getter
    @Setter
    private String targetUserEthID;

    public UserClaim(ClaimDTO claimDTO, String targetUserEthID) {
        super(claimDTO);
        this.targetUserEthID = targetUserEthID;
    }
}
