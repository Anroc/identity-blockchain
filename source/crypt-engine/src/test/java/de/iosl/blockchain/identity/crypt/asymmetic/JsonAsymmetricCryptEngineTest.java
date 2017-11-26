package de.iosl.blockchain.identity.crypt.asymmetic;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.TestEntity;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonAsymmetricCryptEngineTest {

	private AsymmetricCryptEngine<Object> jsonCryptEngine;

	private static final TestEntity DATA = new TestEntity("Hello World", 1337);

	@Before
	public void setup() {
		jsonCryptEngine = CryptEngine.generate()
				.with(AsymmetricCryptEngine.DEFAULT_BIT_SECURITY)
				.json()
				.rsa();
	}

	@Test
	public void generateAndVerifySignature() throws Exception {
		String mac = jsonCryptEngine.sign(DATA);
		boolean verified = jsonCryptEngine.isSignatureAuthentic(mac, DATA, jsonCryptEngine.getPublicKey());

		assertThat(verified).isTrue();
	}

	@Test
	public void generateAndVerifySignatureFailsOnWrongKey() throws Exception {
		String mac = jsonCryptEngine.sign(DATA);
		boolean verified = jsonCryptEngine.isSignatureAuthentic(mac, DATA, new StringAsymmetricCryptEngine().getPublicKey());

		assertThat(verified).isFalse();
	}
}

