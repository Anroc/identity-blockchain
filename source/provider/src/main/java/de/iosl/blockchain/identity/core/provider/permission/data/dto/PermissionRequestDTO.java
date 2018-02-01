package de.iosl.blockchain.identity.core.provider.permission.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

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

    @ApiModelProperty()
    private Set<String> requiredClaims = new HashSet<>();

    @ApiModelProperty()
    private Set<String> optionalClaims = new HashSet<>();

    @Valid
    @ApiModelProperty()
    private Set<ClosureRequestDTO> closureRequests = new HashSet<>();
}
