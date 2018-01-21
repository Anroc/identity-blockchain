package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGrand {

    @Field
    @NotBlank
    private String permissionContractAddress;

    @Field
    @NotEmpty
    private Map<String, Boolean> requiredClaimGrants;

    @Field
    @NotNull
    private Map<String, Boolean> optionalClaimGrants = new HashMap<>();

    public static PermissionGrand init(
            @NonNull String permissionContractAddress,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims) {

        return  new PermissionGrand(
                permissionContractAddress,
                requiredClaims.stream().collect(Collectors.toMap(s -> s, s -> Boolean.FALSE)),
                optionalClaims.stream().collect(Collectors.toMap(s -> s, s -> Boolean.FALSE))
        );
    }
}
