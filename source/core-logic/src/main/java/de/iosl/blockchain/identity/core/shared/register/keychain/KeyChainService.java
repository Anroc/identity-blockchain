package de.iosl.blockchain.identity.core.shared.register.keychain;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.core.shared.register.keychain.data.EncryptedKeyChain;
import de.iosl.blockchain.identity.core.shared.register.keychain.data.EncryptedKeySpec;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.symmetric.PasswordBasedCryptEngine;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.*;

@Slf4j
@Service
public class KeyChainService {

	private static final String WALLET_DIR = System.getProperty("user.home") + File.separator
			+ ".ethereum" + File.separator + "blockchain-identity" + File.separator;
	private static final String KEY_CHAIN = "keychain.json";

	private ObjectMapper objectMapper = new ObjectMapper();

	public String getDefaultWalletFile() {
		return WALLET_DIR + KEY_CHAIN;
	}

	public String getDefaultWalletDir() {
		return WALLET_DIR;
	}

	public void createDir(String path) {
		Paths.get(path).toFile().mkdirs();
	}

	public void saveKeyChain(@NonNull KeyPair keyPair, @NonNull String path, @NonNull String password) throws IOException {
		PasswordBasedCryptEngine passwordBasedCryptEngine = CryptEngine.generate().pbe(password);

		EncryptedKeySpec publicKeySepc = generateEncryptedKeySpec(keyPair.getPublic(), passwordBasedCryptEngine);
		EncryptedKeySpec privateKeySepc = generateEncryptedKeySpec(keyPair.getPrivate(), passwordBasedCryptEngine);

		EncryptedKeyChain encryptedKeyChain = new EncryptedKeyChain(
				privateKeySepc,
				publicKeySepc,
				passwordBasedCryptEngine.getAlgorithm(),
				passwordBasedCryptEngine.getBitSecurity()
		);

		File file = getKeyChain(path);
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		objectMapper.writeValue(file, encryptedKeyChain);
	}

	public KeyPair readKeyChange(@NonNull String path, @NonNull String password) throws IOException {
		File file = getKeyChain(path);
		if(file.exists()) {
			EncryptedKeyChain encryptedKeyChain = objectMapper.reader().forType(EncryptedKeyChain.class).readValue(file);

			PasswordBasedCryptEngine passwordBasedCryptEngine = CryptEngine.generate().pbe(password);
			PrivateKey privateKey = getPrivateKey(encryptedKeyChain.getPrivateKey(), passwordBasedCryptEngine);
			PublicKey publicKey = getPublicKey(encryptedKeyChain.getPublicKey(), passwordBasedCryptEngine);
			return new KeyPair(publicKey, privateKey);
		} else {
			throw new IOException("Could not find file.");
		}
	}

	private File getKeyChain(@NonNull String path) {
		return Paths.get(path).toFile();
	}

	private EncryptedKeySpec generateEncryptedKeySpec(Key key, PasswordBasedCryptEngine cryptEngine) {
		Key secretKey = cryptEngine.getSymmetricCipherKey();
		String encryptedKey = KeyConverter.from(key).toBase64();
		try {
			return new EncryptedKeySpec(
					cryptEngine.encrypt(encryptedKey, secretKey),
					KeyConverter.from(cryptEngine.getIv()).toBase64()
			);
		} catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
			throw new ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private PrivateKey getPrivateKey(@NonNull EncryptedKeySpec encryptedKeySpec, @NonNull PasswordBasedCryptEngine cryptEngine) {
		return KeyConverter.from(decryptKey(encryptedKeySpec, cryptEngine)).toPrivateKey();
	}

	private PublicKey getPublicKey(@NonNull EncryptedKeySpec encryptedKeySpec, @NonNull PasswordBasedCryptEngine cryptEngine) {
		return KeyConverter.from(decryptKey(encryptedKeySpec, cryptEngine)).toPublicKey();
	}

	private String decryptKey(@NonNull EncryptedKeySpec encryptedKeySpec, @NonNull PasswordBasedCryptEngine cryptEngine) {
		byte[] iv = KeyConverter.from(encryptedKeySpec.getIv()).toByes();

		cryptEngine.setIv(iv);
		try {
			return cryptEngine.decrypt(encryptedKeySpec.getKey(), cryptEngine.getSymmetricCipherKey());
		} catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
			throw new ServiceException("Could not access key chain.", e, HttpStatus.FORBIDDEN);
		}
	}
}
