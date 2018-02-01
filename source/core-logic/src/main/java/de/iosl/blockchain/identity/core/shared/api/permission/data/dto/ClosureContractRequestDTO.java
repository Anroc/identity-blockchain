package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosureContractRequestDTO {

    @NotBlank
    private String claimID;

    @NotNull
    private ClaimOperation claimOperation;

    @NotNull
    private Object staticValue;

}

