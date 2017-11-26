package de.iosl.blockchain.identity.crypt.symmetic;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.StringSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.SymmetricCryptEngine;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringSymmetricCryptEngineTest {

	private SymmetricCryptEngine<String> stringCryptEngine;

	private static final String MESSAGE = "Hello world!";
	private static final String MESSAGE_UTF_8 = "Hüllü wörld!";

	@Before
	public void setup() {
		stringCryptEngine = CryptEngine.generate()
				.with(128)
				.string()
				.aes();
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
	public void encryptDecryptWithLongMessageTest() throws Exception {
		encryptDecrypt("Used with a proper block-chaining mode, the same derived key can be used to encrypt many messages."
				+ " In CBC, a random initialization vector (IV) is generated for each message, yielding different cipher "
				+ "text even if the plain text is identical. CBC may not be the most secure mode available to you "
				+ "(see AEAD below); there are many other modes with different security properties, but they all "
				+ "use a similar random input. In any case, the outputs of each encryption operation are the cipher text "
				+ "and the initialization vector:");
	}

	private void encryptDecrypt(String message) throws Exception {
		encryptDecrypt(stringCryptEngine, message);
	}

	private void encryptDecrypt(SymmetricCryptEngine<String> engine, String message) throws Exception {
		String encryptedBase64 = engine.encrypt(message, engine.getSymmetricCipherKey());
		byte[] encryptedText = Base64.decode(encryptedBase64);

		assertThat(encryptedText).isNotEqualTo(message.getBytes(AsymmetricCryptEngine.CHAR_ENCODING));

		String decrypt = engine.decrypt(encryptedBase64, engine.getSymmetricCipherKey());

		assertThat(decrypt).isEqualTo(message);
	}
}
