package de.iosl.blockchain.identity.core.provider.user.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.PayloadDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ProviderDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTOV1 {

    @NotBlank
    private String id;
    private Date modificationDate;
    @Valid
    private ProviderDTO provider;
    @Valid
    private PayloadDTO claimValue;

    @Valid
    private List<SignedRequest<Closure>> signedClosures;

    public ClaimDTOV1(@NonNull ClaimDTO claimDTO) {
        this.id = claimDTO.getId();
        if(claimDTO.getSignedClaimDTO() != null) {
            this.modificationDate = claimDTO.getSignedClaimDTO().getPayload().getModificationDate();
            this.provider = claimDTO.getSignedClaimDTO().getPayload().getProvider();
            this.claimValue = claimDTO.getSignedClaimDTO().getPayload().getClaimValue();
        }
        this.signedClosures = claimDTO.getSignedClosures();
    }
}
