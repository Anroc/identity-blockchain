package de.iosl.blockchain.identity.core.provider.permission.data;

import de.iosl.blockchain.identity.core.provider.permission.data.dto.ClosureRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ClosureRequest {

    private String claimID;
    private ClaimOperation claimOperation;
    private Object staticValue;

    public ClosureRequest(@NonNull ClosureRequestDTO closureRequestDTO) {
        this.claimID = closureRequestDTO.getClaimID();
        this.claimOperation = closureRequestDTO.getClaimOperation();
        this.staticValue = closureRequestDTO.getStaticValue();
    }
}
