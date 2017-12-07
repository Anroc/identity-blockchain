package de.iosl.blockchain.identity.core.user.register;

import de.iosl.blockchain.identity.core.shared.register.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.shared.register.registry.DiscoveryClient;
import de.iosl.blockchain.identity.core.user.register.data.LoginRequest;
import de.iosl.blockchain.identity.core.user.register.data.LoginResponse;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;

@RestController
@RequestMapping("/account")
public class RegisterController {

    private static final String ETHEREUM_ADDR_MOCK = EthereumSigner
            .addressFromPublicKey(BigInteger.valueOf(98_123_612_091_28L));
    @Autowired
    private KeyChainService keyChainService;
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/register")
    public LoginResponse register(@RequestBody @Valid LoginRequest loginRequest)
            throws IOException {
        String password = loginRequest.getPassword();
        KeyPair keyPair = CryptEngine.generate().with(1024).string().rsa()
                .getAsymmetricCipherKeyPair();
        CryptEngine.KEY_CHAIN = keyPair;

        // String ethID = UUID.randomUUID().toString();
        // discoveryClient.register();

        keyChainService.createDir(keyChainService.getDefaultWalletDir());
        keyChainService
                .saveKeyChain(keyPair, keyChainService.getDefaultWalletFile(),
                        password);

        // TODO: replace with real eth ID

        return new LoginResponse(ETHEREUM_ADDR_MOCK);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest)
            throws IOException {
        String password = loginRequest.getPassword();

        KeyPair keyPair = keyChainService
                .readKeyChange(keyChainService.getDefaultWalletFile(),
                        password);
        CryptEngine.KEY_CHAIN = keyPair;

        // String ethID = UUID.randomUUID().toString();
        // discoveryClient.register();

        // TODO: replace with real eth ID
        return new LoginResponse(ETHEREUM_ADDR_MOCK);
    }

    @PostMapping("/logout")
    public void logout() {
        // TODO: unregister from discovery Service
        CryptEngine.KEY_CHAIN = null;
    }
}
