package de.iosl.blockchain.identity.core.provider.user.data.dto;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String ethId;
    private String publicKey;
    private String registerContractAddress;

    @Valid
    @NotNull
    private Set<ClaimDTO> claims;

    public UserDTO(@NonNull User user) {
        this.id = user.getId();
        this.ethId = user.getEthId();
        this.publicKey = user.getPublicKey();
        this.registerContractAddress = user.getRegisterContractAddress();
        claims = user.getClaims().stream()
                .map(ClaimDTO::new).collect(Collectors.toSet());
    }

    public User toUser(@NonNull String id) {
        return new User(
                id,
                getPublicKey(),
                getEthId(),
                getRegisterContractAddress(),
                new ArrayList<>(),
                getClaims().stream().map(ProviderClaim::new).collect(Collectors.toSet()));
    }
}
