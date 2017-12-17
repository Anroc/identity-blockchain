package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.user.data.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class LoginController {

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return new ArrayList<>();
    }

    @PostMapping
    public UserResponse createUser() {
        return null;
    }
}
