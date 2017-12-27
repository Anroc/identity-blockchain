package state.identity.blockchain.iosl.de.blockidclientqrscanner.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.spongycastle.util.encoders.Base64;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class EthereumSigner {

    private ObjectMapper objectMapper = new ObjectMapper();

    public static String addressFromPublicKey(BigInteger publicKey) {
        return Numeric.prependHexPrefix(Keys.getAddress(publicKey));
    }

    public String getSHA256Hash(String plain) {
        try {
            return new String(
                    Base64.encode(MessageDigest.getInstance("SHA-256")
                            .digest(plain.getBytes("UTF-8"))),
                    Charset.forName("UTF-8")
            );
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] hash(Object object) {
        try {
            return getSHA256Hash(
                    objectMapper.writeValueAsString(object)
            ).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Sign.SignatureData sign(Object payload, ECKeyPair ecKeyPair) {
        return Sign.signMessage(hash(payload), ecKeyPair);
    }

    public boolean verifySignature(Object payload,
            Sign.SignatureData signatureData, BigInteger publicKey) {
        return publicKey.equals(publicKeyFromSignature(payload, signatureData));
    }

    public boolean verifySignature(Object payload,
            Sign.SignatureData signatureData, String address) {
        return address.equals(
                addressFromPublicKey(
                        publicKeyFromSignature(payload, signatureData)
                )
        );
    }

    private BigInteger publicKeyFromSignature(Object payload,
            Sign.SignatureData signatureData) {
        try {
            return Sign.signedMessageToKey(hash(payload), signatureData);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
