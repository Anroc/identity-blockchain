package de.iosl.blockchain.identity.crypt.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;
import lombok.NoArgsConstructor;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

@NoArgsConstructor
public class EthereumSigner {

	private ObjectMapper objectMapper = new ObjectMapper();
	private StringAsymmetricCryptEngine stringAsymmetricCryptEngine = new StringAsymmetricCryptEngine();

	private byte[] hash(Object object) {
		try {
			return stringAsymmetricCryptEngine.getSHA256Hash(
					objectMapper.writeValueAsString(object)
			).getBytes();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public Sign.SignatureData sign(Object payload, ECKeyPair ecKeyPair) {
		return Sign.signMessage(hash(payload), ecKeyPair);
	}

	public boolean verifySignature(Object payload, Sign.SignatureData signatureData, BigInteger publicKey) {
		return publicKey.equals(publicKeyFromSignature(payload, signatureData));
	}

	public boolean verifySignature(Object payload, Sign.SignatureData signatureData, String address) {
		return address.equals(
				addressFromPublicKey(
						publicKeyFromSignature(payload, signatureData)
				)
		);
	}

	private BigInteger publicKeyFromSignature(Object payload, Sign.SignatureData signatureData) {
		try {
			return Sign.signedMessageToKey(hash(payload), signatureData);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}

	public static String addressFromPublicKey(BigInteger publicKey) {
		return Numeric.prependHexPrefix(Keys.getAddress(publicKey));
	}
}
