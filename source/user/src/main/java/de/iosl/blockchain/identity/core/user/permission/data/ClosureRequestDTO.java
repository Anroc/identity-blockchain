package de.iosl.blockchain.identity.core.user.permission.data;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosureRequestDTO {
    @NotEmpty
    private String claimID;
    @NotNull
    private ClaimOperation claimOperation;
    @NotEmpty
    private ValueHolder staticValue;

    private String description;
    private boolean expressionResult;

    boolean approved;

    public ClosureRequestDTO(@NonNull ClosureRequest closureRequest) {
        this.claimID = closureRequest.getClaimID();
        this.claimOperation = closureRequest.getClaimOperation();
        this.staticValue = closureRequest.getStaticValue();
        this.description = closureRequest.getDescription();
        this.expressionResult = closureRequest.isExpressionResult();
        this.approved = false;
    }
}
