package de.iosl.blockchain.identity.core.user.permission.data;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(
            value = "Genereated UUID",
            example = "e5dffa5e-dd75-496c-90dd-630c68d56a3c",
            required = false
    )
    private String id;

    @ApiModelProperty(
            value = "The ethereum ID of the provider issued the request",
            example = "0x3c56cfa07f6d65d113ac9fcd1326355ae6db1083",
            required = false
    )
    private String requestingProvider;

    @ApiModelProperty(
            value = "The ethereum ID of the provider holding the claims that are requested.",
            example = "0x9a06cfa07f6d65d113ac9fcd1326355ae6db1083",
            required = false
    )
    private String issuedProvider;

    @ApiModelProperty(
            value = "The address of the permission contract holding refering this values.",
            example = "0x1d78cfa07f6d65d113ac9fcd1326355ae6db1083"
    )
    private String permissionContractAddress;

    @ApiModelProperty(
            value = "Map from required claimIDs to approved by the frontend.",
            required = false,
            notes = "If not present please send an empty map."
    )
    private Map<String, Boolean> requiredClaims;

    @ApiModelProperty(
            value = "Map from optional claimIDs to approved by the frontend.",
            required = false,
            notes = "If not present please send an empty map."
    )
    private Map<String, Boolean> optionalClaims;

    @ApiModelProperty(
            value = "List of closure objects. Set the n'approved flag' inside them.",
            required = false
    )
    private Set<ClosureRequestDTO> closureRequestDTO;


    public PermissionRequestDTO(@NonNull PermissionRequest permissionRequest) {
        this.id = permissionRequest.getId();
        this.requestingProvider = permissionRequest.getRequestingProvider();
        this.issuedProvider = permissionRequest.getIssuedProvider();
        this.permissionContractAddress = permissionRequest.getPermissionContractAddress();

        this.requiredClaims = permissionRequest.getRequiredClaims();
        this.optionalClaims = permissionRequest.getOptionalClaims();

        if(permissionRequest.getClosureRequests() != null) {
            this.closureRequestDTO = permissionRequest.getClosureRequests()
                    .stream()
                    .map(ClosureRequestDTO::new).collect(
                            Collectors.toSet());
        } else {
            this.closureRequestDTO = null;
        }
    }
}
