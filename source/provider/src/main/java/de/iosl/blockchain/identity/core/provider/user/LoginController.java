package de.iosl.blockchain.identity.core.provider.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/claims")
public class LoginController {

    @GetMapping
    public List<String> getAllClaims() {
        return new ArrayList<>();
    }
}
