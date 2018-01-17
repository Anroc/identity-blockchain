package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApprovedClaim extends BasicEthereumDTO {

    @NotBlank
    private String claimId;
    @NotBlank
    private String providerEthId;

    public ApprovedClaim(String ethID, String claimId, String providerEthId) {
        super(ethID);
        this.claimId = claimId;
        this.providerEthId = providerEthId;
    }
}
