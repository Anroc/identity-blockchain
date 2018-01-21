package de.iosl.blockchain.identity.core.user.permission.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequestDTO {

    private String id;
    private String requestingProvider;
    private String issuedProvider;
    private String permissionContractAddress;

    @NotEmpty
    private Map<String, Boolean> requiredClaims;
    private Map<String, Boolean> optionalClaims;

    public PermissionRequestDTO(@NonNull PermissionRequest permissionRequest) {
        this.id = permissionRequest.getId();
        this.requestingProvider = permissionRequest.getRequestingProvider();
        this.issuedProvider = permissionRequest.getIssuedProvider();
        this.permissionContractAddress = permissionRequest.getPermissionContractAddress();

        this.requiredClaims = permissionRequest.getRequiredClaims();
        this.optionalClaims = permissionRequest.getOptionalClaims();
    }
}
