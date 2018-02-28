package de.iosl.blockchain.identity.core.shared.api.permission.data;

import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosureContractRequest implements Serializable {

    private static final long serialVersionUID = -487991492884225033L;

    @Valid
    @NotNull
    private ClosureContractRequestPayload closureContractRequestPayload;

    /**
     * Set by user.
     */
    @Valid
    private ECSignature ecSignature;

    public ClosureContractRequest(ClosureContractRequestDTO closureContractRequestDTO) {
        this.closureContractRequestPayload = new ClosureContractRequestPayload(
                null,
                closureContractRequestDTO.getClaimID(),
                closureContractRequestDTO.getClaimOperation(),
                closureContractRequestDTO.getStaticValue()
        );
    }

    public static ClosureContractRequest init(@NonNull String claimID, @NonNull ClaimOperation claimOperation, @NonNull Object staticValue) {
        return new ClosureContractRequest(
                new ClosureContractRequestPayload(
                        claimID,
                        claimOperation,
                        new ValueHolder(staticValue)
                ),
                null
        );
    }

}