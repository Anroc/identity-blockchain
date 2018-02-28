package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionContractCreationDTO extends BasicEthereumDTO {

    @NotNull
    private Set<String> requiredClaims;

    @NotNull
    private Set<String> optionalClaims;

    @Valid
    @NotNull
    private Set<ClosureContractRequestDTO> closureContractRequestDTOs;

    public PermissionContractCreationDTO(
            @NonNull String ethID,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims,
            @NonNull Set<ClosureContractRequestDTO> closureContractRequestDTOS) {

        super(ethID);
        this.requiredClaims = requiredClaims;
        this.optionalClaims = optionalClaims;
        this.closureContractRequestDTOs = closureContractRequestDTOS;
    }
}
