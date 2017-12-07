package de.iosl.blockchain.identity.crypt.symmetric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.Key;

public class JsonSymmetricCryptEngine extends StringSymmetricCryptEngine {

	private final ObjectMapper objectMapper;

	public JsonSymmetricCryptEngine(int bitSecurity) {
		super(bitSecurity);
		this.objectMapper = new ObjectMapper();
	}

	public String encryptEntity(Object data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return super.encrypt(objectToString(data), key);
	}

	public <T> T decryptEntity(String data, Key key, Class<T> clazz) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		try {
			return objectMapper.reader().forType(clazz).readValue(
					super.decrypt(data, key)
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
