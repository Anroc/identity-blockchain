package de.iosl.blockchain.identity.crypt.symmetic;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.TestEntity;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.JsonSymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.SymmetricCryptEngine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonCryptEngineTest {

	private JsonSymmetricCryptEngine jsonCryptEngine;

	private static final TestEntity DATA = new TestEntity("Hello World", 1337);
	private static final TestEntity DATA_UTF_8 = new TestEntity("Hüllü wörld!", 1337);

	@Before
	public void setup() {
		jsonCryptEngine = (JsonSymmetricCryptEngine) CryptEngine.generate()
				.with(128)
				.json()
				.aes();
	}

	@Test
	public void encryptDecryptWithMessageTest() throws Exception {
		encryptDecrypt(DATA);
	}

	@Test
	public void encryptDecryptWithUTF8MessageTest() throws Exception {
		encryptDecrypt(DATA_UTF_8);
	}

	private void encryptDecrypt(TestEntity message) throws Exception {
		encryptDecrypt(jsonCryptEngine, message);
	}

	private void encryptDecrypt(JsonSymmetricCryptEngine engine, TestEntity data) throws Exception {

		String encryptedBase64 = engine.encrypt(data, engine.getSymmetricCipherKey());
		byte[] encryptedText = Base64.decode(encryptedBase64);

		assertThat(encryptedText).isNotEqualTo(new ObjectMapper().writer().writeValueAsString(data).getBytes("UTF-8"));

		TestEntity decrypt = engine.decrypt(encryptedBase64, engine.getSymmetricCipherKey(), TestEntity.class);

		assertThat(decrypt).isEqualTo(data);
	}
}
