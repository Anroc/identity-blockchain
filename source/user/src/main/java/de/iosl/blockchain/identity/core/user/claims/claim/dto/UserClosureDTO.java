package de.iosl.blockchain.identity.core.user.claims.claim.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.claims.ClosureExpression;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClosureDTO {

    @Valid
    @NotNull
    private SignedRequest<Closure> signedClosure;

    @NotBlank
    private String blindedDescription;

    public UserClosureDTO(@NonNull  SignedRequest<Closure> closureSignedRequest) {
        this.signedClosure = closureSignedRequest;
        this.blindedDescription = ClosureExpression.buildBlindedClosureDescription(closureSignedRequest.getPayload());
    }
}
