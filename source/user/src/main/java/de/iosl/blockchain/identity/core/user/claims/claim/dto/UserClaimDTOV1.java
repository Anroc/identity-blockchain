package de.iosl.blockchain.identity.core.user.claims.claim.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.PayloadDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ProviderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClaimDTOV1 {

    private String id;
    private Date modificationDate;
    @Valid
    private ProviderDTO provider;
    @Valid
    private PayloadDTO claimValue;

    @Valid
    private List<UserClosureDTO> singedUserClosure;

    public UserClaimDTOV1(@NonNull UserClaimDTO userClaimDTO) {
        this.id = userClaimDTO.getId();
        if(userClaimDTO.getSignedClaimDTO() != null) {
            this.modificationDate = userClaimDTO.getSignedClaimDTO().getPayload().getModificationDate();
            this.provider = userClaimDTO.getSignedClaimDTO().getPayload().getProvider();
            this.claimValue = userClaimDTO.getSignedClaimDTO().getPayload().getClaimValue();
        }

        this.singedUserClosure = userClaimDTO.getSignedUserClosures();
    }
}
