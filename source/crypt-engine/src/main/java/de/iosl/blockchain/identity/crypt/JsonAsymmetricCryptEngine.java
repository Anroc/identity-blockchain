package de.iosl.blockchain.identity.crypt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;

public class JsonAsymmetricCryptEngine extends AsymmetricCryptEngine<Object> {

	private final ObjectMapper objectMapper;
	private final StringAsymmetricCryptEngine stringCryptEngine;

	public JsonAsymmetricCryptEngine(int bitSecurity) {
		super(bitSecurity);
		this.objectMapper = new ObjectMapper();
		this.stringCryptEngine = new StringAsymmetricCryptEngine(bitSecurity);
	}

	@Override
	public String encrypt(Object data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return stringCryptEngine.encrypt(objectToString(data), key);
	}

	@Override
	public Object decrypt(String data, Key key) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return stringCryptEngine.decrypt(objectToString(data), key);
	}

	public <T> T decrypt(String data, Key key, Class<T> clazz) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return (T) decrypt(data, key);
	}

	@Override
	public String sign(Object data) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return stringCryptEngine.sign(objectToString(data));
	}

	@Override
	public String getSHA256Hash(Object data) {
		return stringCryptEngine.getSHA256Hash(objectToString(data));
	}

	@Override
	public boolean isSignatureAuthentic(String mac, Object data, PublicKey publicKey) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		return stringCryptEngine.isSignatureAuthentic(mac, objectToString(data), publicKey);
	}

	private String objectToString(Object data) {
		try {
			return objectMapper.writer().writeValueAsString(data);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
