package de.iosl.blockchain.identity.core.provider.permission.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> requiredClaims = new ArrayList<>();

    @ApiModelProperty()
    private List<String> optionalClaims = new ArrayList<>();

    @Valid
    @ApiModelProperty()
    private List<ClosureRequestDTO> closureRequests = new ArrayList<>();
}
