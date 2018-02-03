package de.iosl.blockchain.identity.core.shared.api.permission.data;

import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequestDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosureContractRequest implements Serializable {

    private static final long serialVersionUID = -487991492884225033L;

    @NotBlank
    private String claimID;

    @NotNull
    private ClaimOperation claimOperation;

    @NotNull
    private Object staticValue;

    public ClosureContractRequest(ClosureContractRequestDTO closureContractRequestDTO) {
        this.claimID = closureContractRequestDTO.getClaimID();
        this.claimOperation = closureContractRequestDTO.getClaimOperation();
        this.staticValue = closureContractRequestDTO.getStaticValue();
    }

}