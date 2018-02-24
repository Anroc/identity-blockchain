package de.iosl.blockchain.identity.core.provider.user.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOV1 {

    private String id;
    private String ethId;
    private String publicKey;
    private String registerContractAddress;

    @Valid
    @NotNull
    private Set<ClaimDTOV1> claims;

    public UserDTOV1(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.ethId = userDTO.getEthId();
        this.publicKey = userDTO.getPublicKey();
        this.registerContractAddress = userDTO.getRegisterContractAddress();

        this.claims = userDTO.getClaims().stream().map(ClaimDTOV1::new).collect(Collectors.toSet());
    }
}
