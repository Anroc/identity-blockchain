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

    private Set<ClosureRequest> closuresRequests;

    public PermissionRequest(@NonNull PermissionRequestDTO permissionRequestDTO) {
        this.ethID = permissionRequestDTO.getUserEthID();
        this.url = permissionRequestDTO.getProviderURL();

        if(permissionRequestDTO.getRequiredClaims() != null) {
            this.requiredClaims = permissionRequestDTO.getRequiredClaims();
        } else {
            this.requiredClaims = new HashSet<>();
        }

        if (permissionRequestDTO.getOptionalClaims() == null) {
            this.optionalClaims = new HashSet<>();
        } else {
            this.optionalClaims = permissionRequestDTO.getOptionalClaims();
        }

        if(permissionRequestDTO.getClosureRequests() != null) {
            this.closuresRequests = permissionRequestDTO.getClosureRequests()
                    .stream()
                    .map(ClosureRequest::new)
                    .collect(Collectors.toSet());
        } else {
            this.closuresRequests = new HashSet<>();
        }
    }

    @JsonIgnore
    public Set<ClosureContractRequestDTO> getClosreRequestsAsClosureContractRequestDTOs() {
        return closuresRequests.stream().map(
                closure -> new ClosureContractRequestDTO(
                        closure.getClaimID(),
                        closure.getClaimOperation(),
                        closure.getStaticValue()
                )
        ).collect(Collectors.toSet());
    }
}
