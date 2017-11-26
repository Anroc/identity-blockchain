package de.iosl.blockchain.identity.crypt.symmetric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.CypherProcessor;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;

public class JsonSymmetricCryptEngine extends SymmetricCryptEngine<Object> {

	private final ObjectMapper objectMapper;
	private final SymmetricCryptEngine<String> stringCryptEngine;

	public JsonSymmetricCryptEngine(int bitSecurity) {
		super(bitSecurity);
		this.objectMapper = new ObjectMapper();
		this.stringCryptEngine = CryptEngine.from(getSymmetricCipherKey()).with(bitSecurity).string().aes();
	}

	@Override
	public String encrypt(Object data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return stringCryptEngine.encrypt(objectToString(data), key);
	}

	@Override
	public Object decrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		try {
			return objectMapper.reader().readValue(
					stringCryptEngine.decrypt(data, key)
			);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public <T> T decrypt(String data, Key key, Class<T> clazz) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		try {
			return objectMapper.reader().forType(clazz).readValue(
					stringCryptEngine.decrypt(data, key)
			);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private String objectToString(Object data) {
		try {
			return objectMapper.writer().writeValueAsString(data);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
