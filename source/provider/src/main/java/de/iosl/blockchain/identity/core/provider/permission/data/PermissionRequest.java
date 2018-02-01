package de.iosl.blockchain.identity.core.provider.permission.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.core.provider.permission.data.dto.PermissionRequestDTO;
import de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ClosureContractRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {

    private String ethID;
    private String url;
    private Set<String> requiredClaims;
    private Set<String> optionalClaims;

    private Set<ClosureRequest> closureRequests;

    public PermissionRequest(@NonNull PermissionRequestDTO permissionRequestDTO) {
        this.ethID = permissionRequestDTO.getUserEthID();
        this.url = permissionRequestDTO.getProviderURL();
        this.requiredClaims = permissionRequestDTO.getRequiredClaims();
        if (permissionRequestDTO.getOptionalClaims() == null) {
            this.optionalClaims = new HashSet<>();
        } else {
            this.optionalClaims = permissionRequestDTO.getOptionalClaims();
        }

        if(permissionRequestDTO.getClosureRequests() != null) {
            this.closureRequests = permissionRequestDTO.getClosureRequests()
                    .stream()
                    .map(ClosureRequest::new)
                    .collect(Collectors.toSet());
        } else {
            this.closureRequests = new HashSet<>();
        }
    }

    @JsonIgnore
    public Set<ClosureContractRequestDTO> getClosreRequestsAsClosureContractRequestDTOs() {
        return closureRequests.stream().map(
                closureRequest -> new ClosureContractRequestDTO(
                        closureRequest.getClaimID(),
                        closureRequest.getClaimOperation(),
                        closureRequest.getStaticValue()
                )
        ).collect(Collectors.toSet());
    }
}
