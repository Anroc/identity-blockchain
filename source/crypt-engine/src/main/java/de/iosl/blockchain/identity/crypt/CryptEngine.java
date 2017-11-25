package de.iosl.blockchain.identity.crypt;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;

/**
 * Shared component for encryption and decryption of messages.
 */
@Data
@Slf4j
@RequiredArgsConstructor
public class CryptEngine {

	public static final String ALGORITHM = "RSA";
	public static final int DEFAULT_BIT_SECURITY = 1024;
	public static final String CHAR_ENCODING = "UTF-8";
	private KeyPair asymmetricCipherKeyPair;

	private final int bitSecurity;

	public CryptEngine() {
		this(DEFAULT_BIT_SECURITY);
		Security.addProvider(new BouncyCastleProvider());
	}

	private KeyPair getAsymmetricCipherKeyPair() {
		if (asymmetricCipherKeyPair == null) {
			this.asymmetricCipherKeyPair = generateKeyPair();
		}

		return this.asymmetricCipherKeyPair;
	}

	/**
	 * Generates a new RSA key pair.
	 *
	 * @return a new Key Pair
	 */
	public KeyPair generateKeyPair() {
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		keyGen.initialize(this.bitSecurity);
		return keyGen.generateKeyPair();
	}

	public PublicKey getPublicKey() {
		return getAsymmetricCipherKeyPair().getPublic();
	}

	public PrivateKey getPrivateKey() {
		return getAsymmetricCipherKeyPair().getPrivate();
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
	public String encrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		try {
			return new String(
					Base64.encode(process(data.getBytes(CHAR_ENCODING), key, Cipher.ENCRYPT_MODE)),
					Charset.forName(CHAR_ENCODING)
			);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

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
	public String decrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return new String(process(Base64.decode(data), key, Cipher.DECRYPT_MODE), Charset.forName(CHAR_ENCODING));
	}

	private byte[] process(byte[] data, Key key, int mode) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(mode, key);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a mac of the given plain string.
	 * To do so this method will call {@link #getSHA256Hash(String)} to create a SHA-256
	 * hash of the given message. Then this hash will be digitally signed with the private key.
	 *
	 * @param plain the plain string that shell be singed
	 * @return the base64 encoded MAC
	 * @throws BadPaddingException on padding mismatch
	 * @throws InvalidKeyException on wrong cipher instance
	 * @throws IllegalBlockSizeException on wrong alignment
	 */
	public String sign(String plain) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		String hash = getSHA256Hash(plain);

		return encrypt(hash, getPrivateKey());
	}

	/**
	 * Verifies a MAC for the given plain string and with the given public key.
	 *
	 * @param mac the mac that shell be verified
	 * @param plain the plain message backed by the mac
	 * @param publicKey the public key that created the mac
	 * @return true if the signature is verified, else flase
	 * @throws BadPaddingException on padding mismatch
	 * @throws InvalidKeyException on wrong cipher instance
	 * @throws IllegalBlockSizeException on wrong alignment
	 */
	public boolean isSignatureAuthentic(String mac, String plain, PublicKey publicKey) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		String hash = getSHA256Hash(plain);

		try {
			return decrypt(mac, publicKey).equals(hash);
		} catch (GeneralSecurityException e) {
			log.warn("Could not decrypt mac with public key.", e);
			return false;
		}
	}

	/**
	 * Calculates the MD5 hash of the given string.
	 *
	 * @param plain the string that shell be hashed
	 * @return the base64 encoded hash
	 */
	public String getSHA256Hash(String plain) {
		try {
			return new String(
					Base64.encode(MessageDigest.getInstance("SHA-256").digest(plain.getBytes(CHAR_ENCODING))),
					Charset.forName(CHAR_ENCODING)
			);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
