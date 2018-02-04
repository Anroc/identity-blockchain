package de.iosl.blockchain.identity.core.provider.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.provider.permission.data.dto.ClosureRequestDTO;
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
public class ClosureRequest {

    @Field
    @NotEmpty
    private String claimID;
    @Field
    @NotNull
    private ClaimOperation claimOperation;
    @Field
    @NotEmpty
    private ValueHolder staticValue;

    @Field
    private boolean approved;

    public ClosureRequest(@NonNull ClosureRequestDTO closureRequestDTO) {
        this.claimID = closureRequestDTO.getClaimID();
        this.claimOperation = closureRequestDTO.getClaimOperation();
        this.staticValue = closureRequestDTO.getStaticValue();
        this.approved = false;
    }
}
