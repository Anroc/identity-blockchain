package de.iosl.blockchain.identity.core.provider.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosureRequestDTO {

    @NotBlank
    @ApiModelProperty(required = true, example = "GIVEN_NAME")
    private String claimID;

    @NotNull
    @ApiModelProperty(required = true, example = "EQ")
    private ClaimOperation claimOperation;

    @NotNull
    @ApiModelProperty(required = true, example = "Hans")
    private Object staticValue;

}
