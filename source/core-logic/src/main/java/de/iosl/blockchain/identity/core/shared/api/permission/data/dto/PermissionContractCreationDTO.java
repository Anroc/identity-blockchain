package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder(alphabetic=true, value = {"ethID", "requiredClaims", "optionalClaims", "closureContractRequestDTOs"})
public class PermissionContractCreationDTO extends BasicEthereumDTO {

    @NotNull
    private List<String> requiredClaims;

    @NotNull
    private List<String> optionalClaims;

    @Valid
    @NotNull
    private List<ClosureContractRequestDTO> closureContractRequestDTOs;

    public PermissionContractCreationDTO(
            @NonNull String ethID,
            @NonNull List<String> requiredClaims,
            @NonNull List<String> optionalClaims,
            @NonNull List<ClosureContractRequestDTO> closureContractRequestDTOS) {

        super(ethID);
        this.requiredClaims = requiredClaims;
        this.optionalClaims = optionalClaims;
        this.closureContractRequestDTOs = closureContractRequestDTOS;
    }
}
