package de.iosl.blockchain.identity.core.provider.user.data.dto;

import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimInformationResponse {

    @NotBlank
    @ApiModelProperty(required = true, example = "GIVEN_NAME")
    private String claimID;

    @NotNull
    @ApiModelProperty(required = true, example = "STRING")
    private ClaimType claimType;

    @NotNull
    @ApiModelProperty(required = true)
    private ClaimOperation[] claimOperations;
}
