package de.iosl.blockchain.identity.core.provider.permission;

import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.permission.data.dto.PermissionRequestDTO;
import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/permissions")
public class PermissionRequestController extends AbstractAuthenticator {

    @Autowired
    private PermissionRequestService permissionRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPermissionRequest(@RequestBody @Valid @NotNull PermissionRequestDTO permissionRequestDTO) {
        checkAuthentication();

        PermissionRequest permissionRequest = new PermissionRequest(permissionRequestDTO);
        permissionRequestService.requestPermission(permissionRequest);
    }
}
