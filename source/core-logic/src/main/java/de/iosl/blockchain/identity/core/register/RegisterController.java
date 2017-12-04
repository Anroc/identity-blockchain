package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.core.register.data.LoginRequest;
import de.iosl.blockchain.identity.core.register.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.register.registry.DiscoveryClient;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.security.KeyPair;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class RegisterController {

	@Autowired
	private KeyChainService keyChainService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@PostMapping("/register")
	public String register(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
		String password = loginRequest.getPassword();
		KeyPair keyPair = CryptEngine.generate().with(1024).string().rsa().getAsymmetricCipherKeyPair();
		CryptEngine.KEY_CHAIN = keyPair;

		// String ethID = UUID.randomUUID().toString();
		// discoveryClient.register();

		keyChainService.createDir(keyChainService.getDefaultWalletDir());
		keyChainService.saveKeyChain(keyPair, keyChainService.getDefaultWalletFile(), password);

		// TODO: replace with real eth ID
		return UUID.randomUUID().toString();
	}

	@PostMapping("/login")
	public String login(@RequestBody @Valid LoginRequest loginRequest) throws IOException {
		String password = loginRequest.getPassword();

		KeyPair keyPair = keyChainService.readKeyChange(keyChainService.getDefaultWalletFile(), password);
		CryptEngine.KEY_CHAIN = keyPair;

		// String ethID = UUID.randomUUID().toString();
		// discoveryClient.register();

		// TODO: replace with real eth ID
		return UUID.randomUUID().toString();
	}

	@PostMapping("/logout")
	public void logout() {
		// TODO: unregister from discovery Service
		CryptEngine.KEY_CHAIN = null;
	}
}
