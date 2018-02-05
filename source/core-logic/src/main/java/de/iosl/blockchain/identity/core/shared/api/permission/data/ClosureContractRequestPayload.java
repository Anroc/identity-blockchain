package de.iosl.blockchain.identity.core.shared.api.permission.data;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClosureContractRequestPayload extends BasicEthereumDTO implements Serializable {

    private static final long serialVersionUID = 137496492824225023L;

    @NotBlank
    private String claimID;

    @NotNull
    private ClaimOperation claimOperation;

    @NotNull
    private ValueHolder staticValue;

    public ClosureContractRequestPayload(String ethID, @NonNull String claimID, @NonNull ClaimOperation claimOperation, @NonNull ValueHolder staticValue) {
        super(ethID);
        this.claimID = claimID;
        this.claimOperation = claimOperation;
        this.staticValue = staticValue;
    }
}
