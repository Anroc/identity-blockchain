package de.iosl.blockchain.identity.core.shared.api.data.dto;

import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {

    @NotBlank
    private String id;

    private Date modificationDate;
    @Valid
    @NotNull
    private ProviderDTO provider;
    @Valid
    @NotNull
    private PayloadDTO claimValue;

    private List<SignedRequest<Closure>> signedClosures;

    public ClaimDTO(@NonNull SharedClaim claim) {
        this.id = claim.getId();
        this.modificationDate = claim.getModificationDate();
        this.provider = new ProviderDTO(claim.getProvider());
        this.claimValue = new PayloadDTO(claim.getClaimValue());
        this.signedClosures = claim.getSignedClosures();
    }
}
