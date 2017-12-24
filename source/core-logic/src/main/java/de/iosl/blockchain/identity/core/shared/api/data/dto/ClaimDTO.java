package de.iosl.blockchain.identity.core.shared.api.data.dto;

import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {

    @NotBlank
    private String id;
    private Date modificationDate;
    private Date creationDate;
    @Valid
    @NotNull
    private ProviderDTO provider;
    @Valid
    @NotNull
    private PayloadDTO claimValue;

    public ClaimDTO(@NonNull SharedClaim claim) {
        this.id = claim.getId();
        this.modificationDate = claim.getModificationDate();
        this.creationDate = claim.getCreationDate();
        this.provider = new ProviderDTO(claim.getProvider());
        this.claimValue = new PayloadDTO(claim.getClaimValue());
    }
}
