package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder(alphabetic=true, value = {"ethID", "claimId", "providerEthId"})
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
