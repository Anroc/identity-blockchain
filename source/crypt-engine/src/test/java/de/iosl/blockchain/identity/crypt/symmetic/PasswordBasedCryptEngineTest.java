package de.iosl.blockchain.identity.crypt.symmetic;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.TestEntity;
import de.iosl.blockchain.identity.crypt.symmetric.JsonSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.PasswordBasedCryptEngine;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordBasedCryptEngineTest extends JsonCryptEngineTest {

	private static final String PASSWD = "passwd";

	@Override
	public JsonSymmetricCryptEngine initCryptEngine() {
		return CryptEngine.generate()
				.with(128)
				.pbe(PASSWD);
	}

}
