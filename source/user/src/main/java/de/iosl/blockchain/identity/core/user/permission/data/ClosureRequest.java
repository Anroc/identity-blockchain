package de.iosl.blockchain.identity.core.user.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequestPayload;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
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
        ClosureContractRequestPayload payload = closureContractRequest.getClosureContractRequestPayload();
        this.claimID = payload.getClaimID();
        this.claimOperation = payload.getClaimOperation();
        this.staticValue = new ValueHolder(payload.getStaticValue());
        this.description = description;
        this.expressionResult = expressionResult;
        this.approved = false;
    }

    public ClosureContractRequestPayload toClosureContentRequestPayload(String ethID) {
        return new ClosureContractRequestPayload(
                ethID,
                this.claimID,
                this.claimOperation,
                this.getStaticValue()
        );
    }
}
