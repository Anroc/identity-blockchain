package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {

    @Valid
    private SignedRequest<SignedClaimDTO> signedClaimDTO;
    @Valid
    private List<SignedRequest<Closure>> signedClosures;

    public ClaimDTO(@NonNull SharedClaim sharedClaim) {
        this.signedClaimDTO = sharedClaim.getSignedClaim();
        this.signedClosures = sharedClaim.getSignedClosures();
    }

    @JsonIgnore
    public String getId() {
        return signedClaimDTO.getPayload().getId();
    }
}
