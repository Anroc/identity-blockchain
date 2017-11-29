package de.iosl.blockchain.identity.crypt.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

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

	public BigInteger verifySignature(Object payload, Sign.SignatureData signatureData) {
		try {
			return Sign.signedMessageToKey(hash(payload), signatureData);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}
}
