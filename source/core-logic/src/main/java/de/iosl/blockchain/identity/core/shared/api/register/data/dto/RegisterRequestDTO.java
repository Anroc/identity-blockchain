package de.iosl.blockchain.identity.core.shared.api.register.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RegisterRequestDTO extends BasicEthereumDTO {

    @NotBlank
    private String publicKey;
    @NotBlank
    private String registerContractAddress;

    public RegisterRequestDTO(String ethID, String publicKey, String registerContractAddress) {
        super(ethID);
        this.publicKey = publicKey;
        this.registerContractAddress = registerContractAddress;
    }
}
