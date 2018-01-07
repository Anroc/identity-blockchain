package de.iosl.blockchain.identity.core.user.account;

import de.iosl.blockchain.identity.core.user.account.data.LoginRequest;
import de.iosl.blockchain.identity.core.user.account.data.LoginResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public LoginResponse register(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        String password = loginRequest.getPassword();
        return new LoginResponse(accountService.register(password));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
        String password = loginRequest.getPassword();
        return new LoginResponse(accountService.login(password));
    }

    @PostMapping("/logout")
    public void logout() {
        accountService.logout();
    }

    @ApiOperation(value = "Creates a QR code from the user credentials",
            notes = "This url is WIP and not stable. Only for demonstration purpose.")
    @GetMapping(value = "/qr-code", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getQRCode(
            @RequestParam(value = "width", defaultValue = "256") int width,
            @RequestParam(value = "height", defaultValue = "256") int height) {
        return accountService.getQRCode(width, height);
    }
}
