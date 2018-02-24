package de.iosl.blockchain.identity.core.provider.user.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequestDTO {

    private String id;
    private String ethId;
    private String publicKey;
    private String registerContractAddress;

    @Valid
    @NotNull
    private Set<UnsignedClaimDTO> claims;
}
