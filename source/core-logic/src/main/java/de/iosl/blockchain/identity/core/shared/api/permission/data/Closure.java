package de.iosl.blockchain.identity.core.shared.api.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Closure extends BasicEthereumDTO {

    @NotBlank
    @Field
    private String claimID;
    @NotNull
    @Field
    private ClaimOperation claimOperation;
    @NotNull
    @Field
    private ValueHolder staticValue;
    @NotBlank
    @Field
    private String userEthId;
    
    private boolean expressionResult;

    @NotNull
    @ApiModelProperty(dataType = "java.util.List")
    private LocalDateTime creationDate;

    public static Closure init(@NonNull ClosureContractRequestPayload closureContractRequestPayload) {
        return new Closure(
                closureContractRequestPayload.getClaimID(),
                closureContractRequestPayload.getClaimOperation(),
                closureContractRequestPayload.getStaticValue(),
                null,
                false,
                null
        );
    }
}
