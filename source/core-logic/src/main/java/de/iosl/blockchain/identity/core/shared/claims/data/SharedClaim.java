package de.iosl.blockchain.identity.core.shared.claims.data;

import de.iosl.blockchain.identity.core.shared.api.data.dto.*;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public abstract class SharedClaim {

    private SignedRequest<SignedClaimDTO> signedClaim;

    @Valid
    private List<SignedRequest<Closure>> signedClosures;

    public abstract String getId();

    public abstract void setId(String id);

    public Date getModificationDate() {
        return getSignedClaim().getPayload().getModificationDate();
    };
    public ProviderDTO getProvider() {
        return getSignedClaim().getPayload().getProvider();
    };
    public PayloadDTO getClaimValue() {
        return getSignedClaim().getPayload().getClaimValue();
    };

    public SharedClaim(ClaimDTO claimDTO) {
        this(claimDTO.getSignedClaimDTO(), claimDTO.getSignedClosures());
        setId(claimDTO.getSignedClaimDTO().getPayload().getId());
    }

}
