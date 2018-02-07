package de.iosl.blockchain.identity.core.provider.permission;

import de.iosl.blockchain.identity.core.provider.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.core.provider.permission.data.dto.PermissionRequestDTO;
import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/permissions")
public class PermissionRequestController extends AbstractAuthenticator {

    @Autowired
    private PermissionRequestService permissionRequestService;

    @PostMapping
    public void createPermissionRequest(@RequestBody @Valid @NotNull PermissionRequestDTO permissionRequestDTO) {
        checkAuthentication();

        PermissionRequest permissionRequest = new PermissionRequest(permissionRequestDTO);
        permissionRequestService.requestPermission(permissionRequest);
    }
}
