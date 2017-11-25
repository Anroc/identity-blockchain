package de.iosl.blockchain.identity.crypt;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;

public class StringSymmetricCryptEngine extends SymmetricCryptEngine<String> {

	public StringSymmetricCryptEngine(int bitSecurity) {
		super(bitSecurity);
	}

	public StringSymmetricCryptEngine() {
		super();
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
	@Override
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
	@Override
	public String decrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return new String(process(Base64.decode(data), key, Cipher.DECRYPT_MODE), Charset.forName(CHAR_ENCODING));
	}


}
