package de.iosl.blockchain.identity.core.register.keychain;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.symmetric.StringSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.SymmetricCryptEngine;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.KeyPair;

@Component
public class KeyChainIO {

	private static final String WALLET_DIR = System.getProperty("user.home") + File.separator
			+ ".ethereum " + File.separator + "blockchain-identity" + File.separator;
	private static final String KEY_CHAIN = "keychain.json";

	public void saveKeyChain(@NonNull KeyPair keyPair, @NonNull String password) {
		KeyChain keyChain = new KeyChain();

		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		byte[] publicKeyBytes = keyPair.getPrivate().getEncoded();


		final String salt = KeyGenerators.string().generateKey();

		TextEncryptor encryptor = Encryptors.text(password, salt);
	}
}
