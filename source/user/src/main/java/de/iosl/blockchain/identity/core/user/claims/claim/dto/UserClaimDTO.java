package de.iosl.blockchain.identity.core.user.claims.claim.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import lombok.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserClaimDTO extends ClaimDTO {

    @Getter(onMethod = @__({@Override, @JsonIgnore}))
    @Setter(onMethod = @__({@Override, @JsonIgnore}))
    private List<SignedRequest<Closure>> signedClosures;

    @Valid
    private List<UserClosureDTO> signedUserClosures;

    public UserClaimDTO(UserClaim userClaim) {
        super(userClaim);
        this.signedUserClosures = buildUserClosureDTO(userClaim.getSignedClosures());
    }

    private final List<UserClosureDTO> buildUserClosureDTO(List<SignedRequest<Closure>> signedClosures) {
        if (signedClosures == null) {
            return null;
        }

        return signedClosures.stream().map(UserClosureDTO::new).collect(Collectors.toList());
    }
}
