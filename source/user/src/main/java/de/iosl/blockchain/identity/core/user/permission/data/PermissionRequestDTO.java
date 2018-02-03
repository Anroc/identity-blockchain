package de.iosl.blockchain.identity.core.user.permission.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequestDTO {

    private String id;
    private String requestingProvider;
    private String issuedProvider;
    private String permissionContractAddress;

    @NotNull
    private Map<String, Boolean> requiredClaims;
    @NotNull
    private Map<String, Boolean> optionalClaims;
    @NotNull
    private Set<ClosureRequestDTO> closureRequestDTO;


    public PermissionRequestDTO(@NonNull PermissionRequest permissionRequest) {
        this.id = permissionRequest.getId();
        this.requestingProvider = permissionRequest.getRequestingProvider();
        this.issuedProvider = permissionRequest.getIssuedProvider();
        this.permissionContractAddress = permissionRequest.getPermissionContractAddress();

        this.requiredClaims = permissionRequest.getRequiredClaims();
        this.optionalClaims = permissionRequest.getOptionalClaims();

        this.closureRequestDTO = permissionRequest.getClosureRequests()
                .stream()
                .map(ClosureRequestDTO::new).collect(
                Collectors.toSet());
    }
}
