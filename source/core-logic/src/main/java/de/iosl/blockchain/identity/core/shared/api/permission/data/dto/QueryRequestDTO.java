package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
public class QueryRequestDTO extends BasicEthereumDTO {

    @NotBlank
    private String query;

    public QueryRequestDTO(String ethID, String query) {
        super(ethID);
        this.query = query;
    }
}
