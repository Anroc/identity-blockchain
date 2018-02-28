package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder(alphabetic = true, value = {"ethID", "permissionContractAddress", "singedClaims", "closureContractRequests"})
public class SignedClaimRequestDTO extends BasicEthereumDTO {

    @NotBlank
    private String permissionContractAddress;

    @NotNull
    private List<SignedRequest<ApprovedClaim>> singedClaims;

    @NotNull
    private List<ClosureContractRequest> closureContractRequests;

    public SignedClaimRequestDTO(String ethID,
            String permissionContractAddress,
            List<SignedRequest<ApprovedClaim>> singedClaims,
            List<ClosureContractRequest> closureContractRequests) {
        super(ethID);
        this.permissionContractAddress = permissionContractAddress;
        this.singedClaims = singedClaims;
        this.closureContractRequests = closureContractRequests;
    }
}
