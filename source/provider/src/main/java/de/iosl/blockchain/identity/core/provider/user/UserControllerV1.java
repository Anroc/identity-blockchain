package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.user.data.dto.*;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.register.data.dto.RegisterRequestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserControllerV1 {
    
    @Autowired
    private UserController userController;

    @Deprecated
    @GetMapping
    @ApiOperation("Gets all users")
    public List<UserDTOV1> getUsers() {
        return userController.getUsers().stream().map(UserDTOV1::new).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @ApiOperation("Gets a user")
    public UserDTOV1 getUser(@PathVariable("userId") final String userId) {
        return new UserDTOV1(userController.getUser(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a new user")
    public UserDTOV1 createUser(@RequestBody @Valid @NonNull UserCreationRequestDTO userRequest) {
        return new UserDTOV1(userController.createUser(userRequest));
    }

    @PutMapping("/{userId}")
    @ApiOperation("Updates a user")
    public UserDTOV1 updateUser(@PathVariable("userId") final String userId,
            @RequestBody @Valid @NonNull UserDTO userRequest) {
        return new UserDTOV1(userController.updateUser(userId, userRequest));
    }

    @DeleteMapping("/{userId}")
    @ApiOperation("Deletes a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") final String userId) {
        userController.removeUser(userId);
    }

    @PostMapping("/{userId}/claim")
    @ApiOperation(value = "Updates or inserts a claim")
    public ClaimDTOV1 createClaim(@PathVariable("userId") final String userId,
            @Valid @RequestBody @NotNull UnsignedClaimDTO unsignedClaimDTO) {
        return new ClaimDTOV1(userController.createClaim(userId, unsignedClaimDTO));
    }

    @DeleteMapping("/{userId}/claim/{claimId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes a claim of a user")
    public void removeClaim(@PathVariable("userId") final String userId, @PathVariable("claimId") final String claimId) {
        userController.removeClaim(userId, claimId);
    }

    @PostMapping("/{userId}/register")
    @ApiOperation(
            value = "Registers a users public key and ethereum ID.",
            notes = "Only the state (Government) can post this information.")
    @ResponseStatus(HttpStatus.OK)
    public void registerCredentialInformation(
            @PathVariable("userId") @NotBlank final String userId,
            @RequestBody @Valid @NonNull SignedRequest<RegisterRequestDTO> registerRequest) {
        userController.registerCredentialInformation(userId, registerRequest);
    }

    @GetMapping("/search")
    @ApiOperation("Search for a user id given query parameter's")
    public List<String> search(
            @RequestParam(value = "givenName", defaultValue = "") String givenName,
            @RequestParam(value = "familyName", defaultValue = "") String familyName) {
        return userController.search(givenName, familyName);
    }

    @GetMapping("/ethID/{ethID}/claimIDs")
    @ApiOperation("Get the claimIDs of a user")
    public Set<ClaimInformationResponse> getClaimInformation(@PathVariable("ethID") @NotBlank String ethID) {
        return userController.getClaimInformation(ethID);
    }
}
