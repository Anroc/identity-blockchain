package de.iosl.blockchain.identity.core.user.register;

import de.iosl.blockchain.identity.core.user.register.data.LoginRequest;
import de.iosl.blockchain.identity.core.user.register.data.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/account")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public LoginResponse register(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        String password = loginRequest.getPassword();
        return new LoginResponse(registerService.register(password));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        String password = loginRequest.getPassword();
        return new LoginResponse(registerService.login(password));
    }

    @PostMapping("/logout")
    public void logout() {
        registerService.logout();
    }
}
