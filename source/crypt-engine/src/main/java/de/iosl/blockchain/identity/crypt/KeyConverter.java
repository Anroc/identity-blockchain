package de.iosl.blockchain.identity.crypt;

import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.SymmetricCryptEngine;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyConverter {

	public KeyProducer from(String base64) {
		return new KeyProducer(Base64.decode(base64));
	}

	@RequiredArgsConstructor
	public class KeyProducer {

		private final byte[] key;

		public PublicKey toPublicKey() {
			try {
				return KeyFactory.getInstance(AsymmetricCryptEngine.ALGORITHM).generatePublic(new X509EncodedKeySpec(key));
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

		public PrivateKey toPrivatekey() {
			try {
				return KeyFactory.getInstance(AsymmetricCryptEngine.ALGORITHM).generatePrivate(new X509EncodedKeySpec(key));
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

		public SecretKey toSymmetricKey() {
			return new SecretKeySpec(key, 0, key.length, SymmetricCryptEngine.ALGORITHM);
		}
	}
}
