package de.iosl.blockchain.identity.crypt.symmetric;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;

public class ObjectSymmetricCryptEngine extends SymmetricCryptEngine<Object> {

    @Override
    public String encrypt(Object data, Key key)
            throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return toBase64String(process(serialize(data), key, Cipher.ENCRYPT_MODE));
    }

    @Override
    public Object decrypt(String data, Key key)
            throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return deserialize(process(fromBase64String(data), key, Cipher.DECRYPT_MODE));
    }

    public <T> T decryptAndCast(String data, Key key, Class<T> clazz) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return (T) decrypt(data, key);
    }

    public byte[] serialize(Object obj) {
        try {
            try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
                try(ObjectOutputStream o = new ObjectOutputStream(b)){
                    o.writeObject(obj);
                }
                return b.toByteArray();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Object deserialize(byte[] bytes) {
        try {
            try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
                try(ObjectInputStream o = new ObjectInputStream(b)){
                    try {
                        return o.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String toBase64String(byte[] bytes) {
        return new String(Base64.encode(bytes), Charset.forName(CHAR_ENCODING));
    }

    private byte[] fromBase64String(String base64) {
        return Base64.decode(base64.getBytes(Charset.forName(CHAR_ENCODING)));
    }
}
