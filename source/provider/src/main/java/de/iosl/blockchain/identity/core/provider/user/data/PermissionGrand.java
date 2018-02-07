package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.provider.permission.data.ClosureRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.*;
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

    @Field
    private List<ClosureRequest> closureRequests = new ArrayList<>();

    public static PermissionGrand init(
            @NonNull String permissionContractAddress,
            @NonNull Set<String> requiredClaims,
            @NonNull Set<String> optionalClaims,
            @NonNull Set<ClosureRequest> closureRequests) {

        List<ClosureRequest> closureRequestsList = new ArrayList<>(closureRequests);
        closureRequestsList.forEach(closureRequest -> closureRequest.setApproved(false));

        return  new PermissionGrand(
                permissionContractAddress,
                requiredClaims.stream().collect(Collectors.toMap(s -> s, s -> Boolean.FALSE)),
                optionalClaims.stream().collect(Collectors.toMap(s -> s, s -> Boolean.FALSE)),
                closureRequestsList
        );
    }
}
