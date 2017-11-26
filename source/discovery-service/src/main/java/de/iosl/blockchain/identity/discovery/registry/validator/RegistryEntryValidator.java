package de.iosl.blockchain.identity.discovery.registry.validator;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
public class RegistryEntryValidator {

	public boolean isValid(@NonNull RegistryEntry registryEntry) {
		PublicKey publicKey = KeyConverter.from(registryEntry.getPayload().getPublicKey()).toPublicKey();

		AsymmetricCryptEngine<Object> cryptEngine = CryptEngine.instance().json().rsa();
		return cryptEngine.isSignatureAuthentic(registryEntry.getMac(), registryEntry.getPayload(), publicKey);
	}

	private boolean validateEthereumAdress(@NonNull String ethID, @NonNull PublicKey publicKey) {
		// todo: implement
		return true;
	}
}
