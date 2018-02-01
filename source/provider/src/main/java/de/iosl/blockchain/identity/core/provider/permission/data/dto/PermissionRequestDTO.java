package de.iosl.blockchain.identity.core.provider.permission.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestDTO {

    @NotBlank
    @ApiModelProperty(required = true, example = "0x123122123123123")
    private String userEthID;

    @NotBlank
    @ApiModelProperty(required = true, example = "https://deutscheBank.de:4123")
    private String providerURL;

    @NotEmpty
    @ApiModelProperty(required = true)
    private Set<String> requiredClaims;

    @ApiModelProperty(required = false)
    private Set<String> optionalClaims = new HashSet<>();

    @Valid
    @ApiModelProperty(required = false)
    private Set<ClosureRequestDTO> closureRequests;
}
