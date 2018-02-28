package de.iosl.blockchain.identity.crypt.asymmetic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.ObjectMapperFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;

public class JsonAsymmetricCryptEngine extends AsymmetricCryptEngine<Object> {
    private final ObjectMapper objectMapper;
    private final AsymmetricCryptEngine<String> stringCryptEngine;

    public JsonAsymmetricCryptEngine(int bitSecurity) {
        super(bitSecurity);
        this.objectMapper = ObjectMapperFactory.create();
        this.stringCryptEngine = CryptEngine.from(getAsymmetricCipherKeyPair())
                .with(bitSecurity)
                .string()
                .rsa();
    }

    @Override
    public String encrypt(Object data, Key key)
            throws BadPaddingException, InvalidKeyException,
            IllegalBlockSizeException {
        return stringCryptEngine.encrypt(objectToString(data), key);
    }

    @Override
    public Object decrypt(String data, Key key)
            throws BadPaddingException, InvalidKeyException,
            IllegalBlockSizeException {
        try {
            return objectMapper.reader().readValue(
                    stringCryptEngine.decrypt(data, key)
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> T decrypt(String data, Key key, Class<T> clazz)
            throws BadPaddingException, InvalidKeyException,
            IllegalBlockSizeException {
        try {
            return objectMapper.reader().forType(clazz).readValue(
                    stringCryptEngine.decrypt(data, key)
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String sign(Object data)
            throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException {
        return stringCryptEngine.sign(objectToString(data));
    }

    @Override
    public String getSHA256Hash(Object data) {
        return stringCryptEngine.getSHA256Hash(objectToString(data));
    }

    @Override
    public boolean isSignatureAuthentic(String mac, Object data,
            PublicKey publicKey) {
        return stringCryptEngine
                .isSignatureAuthentic(mac, objectToString(data), publicKey);
    }

    private String objectToString(Object data) {
        try {
            return objectMapper.writer().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
