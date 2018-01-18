package de.iosl.blockchain.identity.core.provider.permission.data;

import de.iosl.blockchain.identity.core.provider.permission.data.dto.PermissionRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {

    private String ethID;
    private String url;
    private Set<String> requiredClaims;
    private Set<String> optionalClaims;

    public PermissionRequest(@NonNull PermissionRequestDTO permissionRequestDTO) {
        this.ethID = permissionRequestDTO.getUserEthID();
        this.url = permissionRequestDTO.getProviderURL();
        this.requiredClaims = permissionRequestDTO.getRequiredClaims();
        if (permissionRequestDTO.getOptionalClaims() == null) {
            this.optionalClaims = new HashSet<>();
        } else {
            this.optionalClaims = permissionRequestDTO.getOptionalClaims();
        }
    }
}
