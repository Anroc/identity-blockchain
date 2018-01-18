package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignedClaimRequestDTO extends BasicEthereumDTO {

    @NotBlank
    private String permissionContractAddress;

    @NotNull
    private List<SignedRequest<ApprovedClaim>> singedClaims;

    public SignedClaimRequestDTO(String ethID,
            String permissionContractAddress,
            List<SignedRequest<ApprovedClaim>> singedClaims) {
        super(ethID);
        this.permissionContractAddress = permissionContractAddress;
        this.singedClaims = singedClaims;
    }
}
