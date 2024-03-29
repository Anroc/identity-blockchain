package de.iosl.blockchain.identity.core.user.permission;

import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.user.permission.data.ClosureRequest;
import de.iosl.blockchain.identity.core.user.permission.data.ClosureRequestDTO;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequestDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionController extends AbstractAuthenticator {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public PermissionRequestDTO findPermissionRequest(@PathVariable("id") final String id) {
        checkAuthentication();

        return new PermissionRequestDTO(getPermissionRequest(id));
    }

    @PutMapping("/{id}")
    public PermissionRequestDTO updatePermissionRequest(
            @PathVariable("id") final String id,
            @Valid @NotNull @RequestBody PermissionRequestDTO permissionRequestDTO) {
        checkAuthentication();

        PermissionRequest permissionRequest = getPermissionRequest(id);

        if(permissionRequestDTO.getOptionalClaims() != null && permissionRequest.getOptionalClaims() != null) {
            permissionRequest.getOptionalClaims().putAll(permissionRequestDTO.getOptionalClaims());
        }
        if(permissionRequestDTO.getRequiredClaims() != null && permissionRequest.getRequiredClaims() != null) {
            permissionRequest.getRequiredClaims().putAll(permissionRequestDTO.getRequiredClaims());
        }
        if(permissionRequestDTO.getClosureRequestDTO() != null && permissionRequest.getClosureRequests() != null) {
            setApprovedFlagForClosures(permissionRequest, permissionRequestDTO);
        }

        if(permissionRequest.getRequiredClaims() != null && permissionRequest.getOptionalClaims() != null) {
            if (permissionRequest.getOptionalClaims().containsValue(true) && permissionRequest.getRequiredClaims().containsValue(false)) {
                throw new ServiceException("Either accept no claims or all required claims.", HttpStatus.BAD_REQUEST);
            }
        }

        return new PermissionRequestDTO(permissionService.updatePermissionRequest(permissionRequest));
    }

    private void setApprovedFlagForClosures(PermissionRequest permissionRequest, PermissionRequestDTO permissionRequestDTO) {
        List<ClosureRequest> closureRequests = permissionRequest.getClosureRequests();
        List<ClosureRequestDTO> closureRequestDTOs = permissionRequestDTO.getClosureRequestDTO();

        for(ClosureRequestDTO searchEntity : closureRequestDTOs) {
            findMatchingClosureRequest(closureRequests, searchEntity)
                    .ifPresent(closureRequest -> closureRequest.setApproved(searchEntity.isApproved()));
        }
    }

    private Optional<ClosureRequest> findMatchingClosureRequest(List<ClosureRequest> searchSet, ClosureRequestDTO searchEntity) {
        return searchSet.stream()
                .filter(
                        closureRequest -> closureRequest.getClaimID().equals(searchEntity.getClaimID()) &&
                                closureRequest.getClaimOperation() == searchEntity.getClaimOperation() &&
                                closureRequest.getStaticValue().equals(searchEntity.getStaticValue())

                ).findAny();
    }

    private PermissionRequest getPermissionRequest(@NonNull String id) {
        return permissionService.findPermissionRequest(id).orElseThrow(
                () -> new ServiceException("Could not find resource with id [%s].", HttpStatus.NOT_FOUND, id)
        );
    }

}
