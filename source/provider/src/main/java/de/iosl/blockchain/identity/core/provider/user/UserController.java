package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.provider.user.data.UserDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable("userId") final String id) {
        return userService.findUser(id)
                .map(UserDTO::new)
                .orElseThrow(
                () -> new ServiceException(
                        "Could not find user with id [%s].",
                        HttpStatus.NOT_FOUND,
                        id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid UserDTO userRequest) {
        User user = userService.insertUser(userRequest.toUser(
                UUID.randomUUID().toString(),
                userRequest
        ));

        return new UserDTO(user);
    }
}
