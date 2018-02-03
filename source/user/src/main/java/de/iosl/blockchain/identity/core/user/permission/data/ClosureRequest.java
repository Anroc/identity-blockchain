package de.iosl.blockchain.identity.core.user.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosureRequest {

    @Field
    @NotEmpty
    private String claimID;
    @Field
    @NotNull
    private ClaimOperation claimOperation;

    @Field
    @Valid
    @NotNull
    private ValueHolder staticValue;

    @Field
    @NotEmpty
    private String description;
    @Field
    private boolean expressionResult;
    @Field
    private boolean approved;

    public ClosureRequest(@NonNull ClosureContractRequest closureContractRequest, @NonNull String description, boolean expressionResult) {
        this.claimID = closureContractRequest.getClaimID();
        this.claimOperation = closureContractRequest.getClaimOperation();
        this.staticValue = new ValueHolder(closureContractRequest.getStaticValue());
        this.description = description;
        this.expressionResult = expressionResult;
        this.approved = false;
    }

    public ClosureContractRequest toClosureContentRequest() {
        return new ClosureContractRequest(
                this.claimID,
                this.claimOperation,
                this.getStaticValue().getUnifiedValue()
        );
    }
}
