package de.iosl.blockchain.identity.crypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonCryptEngineTest {

	private JsonAsymmetricCryptEngine jsonCryptEngine;

	private static final TestEntity DATA = new TestEntity("Hello World", 1337);
	private static final TestEntity DATA_UTF_8 = new TestEntity("Hüllü wörld!", 1337);

	@Before
	public void setup() {
		jsonCryptEngine = new JsonAsymmetricCryptEngine(AsymmetricCryptEngine.DEFAULT_BIT_SECURITY);
	}

	@Test
	@Ignore
	public void encryptDecryptWithMessageTest() throws Exception {
		encryptDecrypt(DATA);
	}

	@Test
	@Ignore
	public void encryptDecryptWithUTF8MessageTest() throws Exception {
		encryptDecrypt(DATA_UTF_8);
	}

	@Test
	@Ignore
	public void encryptDecryptWith2048BitSecurity() throws Exception {
		encryptDecrypt(new JsonAsymmetricCryptEngine(2048), DATA);
	}

	@Test
	@Ignore
	public void encryptDecryptWith4096BitSecurity() throws Exception {
		encryptDecrypt(new JsonAsymmetricCryptEngine(4096), DATA);
	}

	@Test
	@Ignore
	public void generateAndVerifySignature() throws Exception {
		String mac = jsonCryptEngine.sign(DATA);
		boolean verified = jsonCryptEngine.isSignatureAuthentic(mac, DATA, jsonCryptEngine.getPublicKey());

		assertThat(verified).isTrue();
	}

	@Test
	@Ignore
	public void generateAndVerifySignatureFailsOnWrongKey() throws Exception {
		String mac = jsonCryptEngine.sign(DATA);
		boolean verified = jsonCryptEngine.isSignatureAuthentic(mac, DATA, new JsonAsymmetricCryptEngine(AsymmetricCryptEngine.DEFAULT_BIT_SECURITY).getPublicKey());

		assertThat(verified).isFalse();
	}


	private void encryptDecrypt(TestEntity message) throws Exception {
		encryptDecrypt(jsonCryptEngine, message);
	}

	private void encryptDecrypt(JsonAsymmetricCryptEngine engine, TestEntity data) throws Exception {
		String encryptedBase64 = engine.encrypt(data, engine.getPublicKey());
		byte[] encryptedText = Base64.decode(encryptedBase64);

		assertThat(encryptedText).isNotEqualTo(new ObjectMapper().writer().writeValueAsString(data));

		TestEntity decrypt = engine.decrypt(encryptedBase64, engine.getPrivateKey(), TestEntity.class);

		assertThat(decrypt).isEqualTo(data);
	}

	@Data
	@AllArgsConstructor
	static class TestEntity {
		private String field;
		private int number;
	}
}
