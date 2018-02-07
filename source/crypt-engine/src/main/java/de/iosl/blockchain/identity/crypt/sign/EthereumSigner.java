package de.iosl.blockchain.identity.crypt.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

@Slf4j
@NoArgsConstructor
public class EthereumSigner {

    private ObjectMapper objectMapper = new ObjectMapper();
    private StringAsymmetricCryptEngine stringAsymmetricCryptEngine = new StringAsymmetricCryptEngine();

    public static String addressFromPublicKey(BigInteger publicKey) {
        return Numeric.prependHexPrefix(Keys.getAddress(publicKey));
    }

    private byte[] hash(Object object) {
        try {
            String objectString = objectMapper.writeValueAsString(object);
            log.info("Hashing object: {}", objectString);

            return stringAsymmetricCryptEngine.getSHA256Hash(
                    objectString
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
        String computedAddress = addressFromPublicKey(
                publicKeyFromSignature(payload, signatureData)
        );

        log.info("Verify Signature.. Given Address: {}, Computed address: {}", address, computedAddress);
        return address.equals(computedAddress);
    }

    private BigInteger publicKeyFromSignature(Object payload,
            Sign.SignatureData signatureData) {
        log.debug("signature data: {}, {}, {}", signatureData.getR(), signatureData.getS(), signatureData.getV());

        try {
            return Sign.signedMessageToKey(hash(payload), signatureData);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
