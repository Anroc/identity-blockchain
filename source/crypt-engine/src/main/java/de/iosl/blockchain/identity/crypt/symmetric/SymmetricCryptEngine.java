package de.iosl.blockchain.identity.crypt.symmetric;

import de.iosl.blockchain.identity.crypt.CypherProcessor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import java.security.*;

@Slf4j
public abstract class SymmetricCryptEngine<T> extends CypherProcessor {

	public static final String ALGORITHM = "AES";
	public static final int DEFAULT_BIT_SECURITY = 256;
	public static final String CHAR_ENCODING = "UTF-8";

	@Setter
	private Key symmetricCipherKey;

	private final int bitSecurity;

	public SymmetricCryptEngine() {
		this(DEFAULT_BIT_SECURITY);
	}

	public SymmetricCryptEngine(int bitSecurity) {
		this.bitSecurity = bitSecurity;
		Security.addProvider(new BouncyCastleProvider());
	}

	public Key getSymmetricCipherKey() {
		if (symmetricCipherKey == null) {
			this.symmetricCipherKey = generateKey();
		}

		return this.symmetricCipherKey;
	}

	/**
	 * Generates a new RSA key pair.
	 *
	 * @return a new Key Pair
	 */
	public Key generateKey() {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		keyGen.init(this.bitSecurity, new SecureRandom());
		return keyGen.generateKey();
	}

	/**
	 * Encrypts the given data with the given key.
	 *
	 * @param data the data that shell be encrypted
	 * @param key the key that will be used (either public or private)
	 * @return the base64 encoded encrypted string.
	 * @throws BadPaddingException on padding mismatch
	 * @throws InvalidKeyException on wrong cipher instance
	 * @throws IllegalBlockSizeException on wrong alignment
	 */
	abstract public String encrypt(T data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException;

	/**
	 * Decrypts the given data with the given key.
	 *
	 * @param data the base64 encoded data that shell be decrypted
	 * @param key the key that will be used (either public or private)
	 * @return the plain string
	 * @throws BadPaddingException on padding mismatch
	 * @throws InvalidKeyException on wrong cipher instance
	 * @throws IllegalBlockSizeException on wrong alignment
	 */
	abstract public T decrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException;

	@Override
	public String getAlgorithm() {
		return ALGORITHM;
	}
}
