package de.iosl.blockchain.identity.core.user.permission;

import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequestDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/permissions")
public class PermissionController extends AbstractAuthenticator {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public PermissionRequest findPermissionRequest(@PathVariable("id") final String id) {
        checkAuthentication();

        return getPermissionRequest(id);
    }

    @PutMapping("/{id}")
    public PermissionRequest updatePermissionRequest(
            @PathVariable("id") final String id,
            @Valid @NotNull @RequestBody PermissionRequestDTO permissionRequestDTO) {
        checkAuthentication();

        PermissionRequest permissionRequest = getPermissionRequest(id);

        permissionRequest.setOptionalClaims(permissionRequestDTO.getOptionalClaims());
        permissionRequest.setRequiredClaims(permissionRequestDTO.getRequiredClaims());
        return permissionService.updatePermissionRequest(permissionRequest);
    }

    private PermissionRequest getPermissionRequest(@NonNull String id) {
        return permissionService.findPermissionRequest(id).orElseThrow(
                () -> new ServiceException("Could not find resource with id [%s].", HttpStatus.NOT_FOUND, id)
        );
    }

}
