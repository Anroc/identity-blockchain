package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionContractCreationDTO extends BasicEthereumDTO {

    @NotEmpty
    private Set<String> requiredClaims;

    @NotNull
    private Set<String> optionalClaims;

    public PermissionContractCreationDTO(String ethID, Set<String> requiredClaims, Set<String> optionalClaims) {
        super(ethID);
        this.requiredClaims = requiredClaims;
        this.optionalClaims = optionalClaims;
    }
}
