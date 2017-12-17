package de.iosl.blockchain.identity.core.provider.user.data;

import de.iosl.blockchain.identity.core.provider.data.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String ethId;
    private String publicKey;

    @Valid
    @NotNull
    private Set<ClaimDTO> claims;

    public UserDTO(@NonNull User user) {
        this.id = user.getId();
        this.ethId = user.getEthId();
        this.publicKey = user.getPublicKey();
        claims = user.getClaimList().stream()
                .map(ClaimDTO::new).collect(Collectors.toSet());
    }

    public User toUser(@NonNull String id, @NonNull UserDTO userDTO) {
        return new User(
                id,
                getPublicKey(),
                getEthId(),
                userDTO.getClaims().stream().map(ClaimDTO::toClaim).collect(Collectors.toSet()));
    }
}
