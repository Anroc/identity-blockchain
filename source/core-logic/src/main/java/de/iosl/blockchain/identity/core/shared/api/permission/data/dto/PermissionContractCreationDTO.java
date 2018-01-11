package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionContractCreationDTO extends BasicEthereumDTO {

    @NotEmpty
    private List<String> claimIds;

    public PermissionContractCreationDTO(String ethID, List<String> claimIds) {
        super(ethID);
        this.claimIds = claimIds;
    }
}
