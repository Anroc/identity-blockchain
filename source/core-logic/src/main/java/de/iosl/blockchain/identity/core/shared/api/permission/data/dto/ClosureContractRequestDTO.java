package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(alphabetic=true, value = {"claimID", "claimOperation", "staticValue"})
public class ClosureContractRequestDTO {

    @NotBlank
    private String claimID;

    @NotNull
    private ClaimOperation claimOperation;

    @NotNull
    private ValueHolder staticValue;

}

