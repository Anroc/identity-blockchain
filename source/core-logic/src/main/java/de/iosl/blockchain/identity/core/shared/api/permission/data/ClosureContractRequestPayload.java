package de.iosl.blockchain.identity.core.shared.api.permission.data;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosureContractRequestPayload extends BasicEthereumDTO implements Serializable {

    @NotBlank
    private String claimID;

    @NotNull
    private ClaimOperation claimOperation;

    @NotNull
    private Object staticValue;

    public ClosureContractRequestPayload(String ethID, @NonNull String claimID, @NonNull ClaimOperation claimOperation, @NonNull Object staticValue) {
        super(ethID);
        this.claimID = claimID;
        this.claimOperation = claimOperation;
        this.staticValue = staticValue;
    }
}
