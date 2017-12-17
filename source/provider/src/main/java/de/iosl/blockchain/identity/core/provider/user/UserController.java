package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.provider.user.data.ClaimDTO;
import de.iosl.blockchain.identity.core.provider.user.data.UserDTO;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation("Gets all users")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @ApiOperation("Gets a user")
    public UserDTO getUser(@PathVariable("userId") final String userId) {
        return new UserDTO(getUserOrFail(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a new user")
    public UserDTO createUser(@RequestBody @Valid @NonNull UserDTO userRequest) {
        User user = userService.insertUser(
                userRequest.toUser(UUID.randomUUID().toString()));

        return new UserDTO(user);
    }

    @PutMapping("/{userId}")
    @ApiOperation("Updates a user")
    public UserDTO updateUser(@PathVariable("userId") final String userId,
            @RequestBody @Valid @NonNull UserDTO userRequest) {
        if(! userService.exists(userId)) {
            throw new ServiceException("Could not find user with id [%s]", HttpStatus.NOT_FOUND, userId);
        }
        User user = userRequest.toUser(userId);
        user = userService.updateUser(user);
        return new UserDTO(user);
    }

    @DeleteMapping("/{userId}")
    @ApiOperation("Deletes a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") final String userId) {
        User user = getUserOrFail(userId);
        userService.removeUser(user);
    }

    @PostMapping("/{userId}/claim")
    @ApiOperation(value = "Updates or inserts a claim")
    public ClaimDTO createClaim(@PathVariable("userId") final String userId,
            @Valid @RequestBody @NotNull  ClaimDTO claimDTO) {
        User user = getUserOrFail(userId);

        SharedClaim claim = userService.createClaim(user, claimDTO.toClaim());
        return new ClaimDTO(claim);
    }

    @DeleteMapping("/{userId}/claim/{claimId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes a claim of a user")
    public void removeClaim(@PathVariable("userId") final String userId, @PathVariable("claimId") final String claimId) {
        User user = getUserOrFail(userId);

        userService.removeClaim(user, claimId);
    }

    private User getUserOrFail(@NonNull final String userId) {
        return userService.findUser(userId)
                .orElseThrow(
                        () -> new ServiceException("Could not find user with userId [%s]", HttpStatus.NOT_FOUND, userId)
                );
    }

}
