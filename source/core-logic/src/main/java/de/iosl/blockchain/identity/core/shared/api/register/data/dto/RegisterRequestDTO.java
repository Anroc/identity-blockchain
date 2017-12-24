package de.iosl.blockchain.identity.core.shared.api.register.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    private String ethereumID;
    @NotBlank
    private String publicKey;
    @NotBlank
    private String registerContractAddress;

}
