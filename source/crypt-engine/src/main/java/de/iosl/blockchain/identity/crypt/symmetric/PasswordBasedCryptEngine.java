package de.iosl.blockchain.identity.crypt.symmetric;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

public class PasswordBasedCryptEngine extends JsonSymmetricCryptEngine {

	public static final String SECURITY_SPECS = "PBKDF2WithHmacSHA256";
	public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	private final String password;
	private static final byte[] SALT = "league_of_legends".getBytes();
	private static final int ITERATIONS = 65536;

	@Getter
	@Setter
	private byte[] iv;

	public PasswordBasedCryptEngine(int bitSecurity, @NonNull String password) {
		super(bitSecurity);
		this.password = password;
	}

	@Override
	public Key generateKey() {
		if(password == null) {
			throw new IllegalArgumentException("Password was not provided.");
		}
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(SECURITY_SPECS);
			KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, ITERATIONS,  getBitSecurity());
			SecretKey tmp = factory.generateSecret(spec);
			return new SecretKeySpec(tmp.getEncoded(), "AES");

		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected byte[] process(byte[] data, Key key, int mode) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		try {
			Cipher cipher = Cipher.getInstance(getAlgorithm());
			if(mode == Cipher.ENCRYPT_MODE) {
				cipher.init(mode, key);
				AlgorithmParameters params = cipher.getParameters();
				setIv(params.getParameterSpec(IvParameterSpec.class).getIV());
			} else if(mode == Cipher.DECRYPT_MODE) {
				cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIv()));
			}
			return cipher.doFinal(data);
		} catch (InvalidParameterSpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getAlgorithm() {
		return ALGORITHM;
	}
}
