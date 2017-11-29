package de.iosl.blockchain.identity.crypt;

import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.asymmetic.JsonAsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.JsonSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.StringSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.SymmetricCryptEngine;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Key;
import java.security.KeyPair;

public class CryptEngine {

	private Key key;
	private KeyPair keyPair;
	private int bitSecurity;
	public static final String CHAR_ENCODING = "UTF-8";

	private CryptEngine(Key key, KeyPair keyPair) {
		this.key = key;
		this.keyPair = keyPair;
		this.bitSecurity = -1;
	}

	public static CryptEngine from(Key key) {
		return new CryptEngine(key, null);
	}

	public static CryptEngine from(KeyPair keyPair) {
		return new CryptEngine(null, keyPair);
	}

	public static CryptEngine generate() {
		return  new CryptEngine(null, null);
	}

	public static CryptEngine instance() {
		return generate();
	}

	public CryptEngine with(int bitSecurity) {
		this.bitSecurity = bitSecurity;
		return this;
	}

	public SubTypeProducer<String> string() {
		return new SubTypeProducer<>(this.key, this.keyPair, this.bitSecurity, String.class);
	}

	public SubTypeProducer<Object> json() {
		return new SubTypeProducer<>(this.key, this.keyPair, this.bitSecurity, Object.class);
	}

	@Data
	@AllArgsConstructor
	public class SubTypeProducer<T> {
		private Key key;
		private KeyPair keyPair;
		private int bitSecurity;

		private Class<T> clazz;

		public SymmetricCryptEngine<T> aes() {
			SymmetricCryptEngine<T> engine;

			if(clazz.equals(String.class)) {
				engine = (SymmetricCryptEngine<T>) new StringSymmetricCryptEngine(
						bitSecurity == -1 ?
								SymmetricCryptEngine.DEFAULT_BIT_SECURITY
								: bitSecurity);
			} else {
				engine = (SymmetricCryptEngine<T>) new JsonSymmetricCryptEngine(
						bitSecurity == -1 ?
								SymmetricCryptEngine.DEFAULT_BIT_SECURITY
								: bitSecurity);
			}

			if(key != null) {
				engine.setSymmetricCipherKey(key);
			}
			return engine;
		}

		public AsymmetricCryptEngine<T> rsa() {

			AsymmetricCryptEngine<T> engine;

			if(clazz.equals(String.class)) {
				engine = (AsymmetricCryptEngine<T>) new StringAsymmetricCryptEngine(
						bitSecurity == -1 ?
								AsymmetricCryptEngine.DEFAULT_BIT_SECURITY
								: bitSecurity);
			} else {
				engine = (AsymmetricCryptEngine<T>) new JsonAsymmetricCryptEngine(
						bitSecurity == -1 ?
								AsymmetricCryptEngine.DEFAULT_BIT_SECURITY
								: bitSecurity);
			}

			if(keyPair != null) {
				engine.setAsymmetricCipherKeyPair(keyPair);
			}
			return engine;

		}

	}
}
