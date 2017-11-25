package de.iosl.blockchain.identity.crypt;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringAsymmetricCryptEngineTest {

	private StringAsymmetricCryptEngine stringCryptEngine;

	private static final String MESSAGE = "Hello world!";
	private static final String MESSAGE_UTF_8 = "Hüllü wörld!";

	@Before
	public void setup() {
		stringCryptEngine = new StringAsymmetricCryptEngine(AsymmetricCryptEngine.DEFAULT_BIT_SECURITY);
	}

	@Test
	public void encryptDecryptWithMessageTest() throws Exception {
		encryptDecrypt(MESSAGE);
	}

	@Test
	public void encryptDecryptWithUTF8MessageTest() throws Exception {
		encryptDecrypt(MESSAGE_UTF_8);
	}

	@Test
	public void encryptDecryptWith2048BitSecurity() throws Exception {
		encryptDecrypt(new StringAsymmetricCryptEngine(2048), MESSAGE);
	}

	@Test
	public void encryptDecryptWith4096BitSecurity() throws Exception {
		encryptDecrypt(new StringAsymmetricCryptEngine(4096), MESSAGE);
	}

	@Test
	public void generateAndVerifySignature() throws Exception {
		String mac = stringCryptEngine.sign(MESSAGE);
		boolean verified = stringCryptEngine.isSignatureAuthentic(mac, MESSAGE, stringCryptEngine.getPublicKey());

		assertThat(verified).isTrue();
	}

	@Test
	public void generateAndVerifySignatureFailsOnWrongKey() throws Exception {
		String mac = stringCryptEngine.sign(MESSAGE);
		boolean verified = stringCryptEngine.isSignatureAuthentic(mac, MESSAGE, new StringAsymmetricCryptEngine().getPublicKey());

		assertThat(verified).isFalse();
	}


	private void encryptDecrypt(String message) throws Exception {
		encryptDecrypt(stringCryptEngine, message);
	}

	private void encryptDecrypt(StringAsymmetricCryptEngine engine, String message) throws Exception {
		String encryptedBase64 = engine.encrypt(message, engine.getPublicKey());
		byte[] encryptedText = Base64.decode(encryptedBase64);

		assertThat(encryptedText).isNotEqualTo(message.getBytes(AsymmetricCryptEngine.CHAR_ENCODING));

		String decrypt = engine.decrypt(encryptedBase64, engine.getPrivateKey());

		assertThat(decrypt).isEqualTo(message);
	}
}
