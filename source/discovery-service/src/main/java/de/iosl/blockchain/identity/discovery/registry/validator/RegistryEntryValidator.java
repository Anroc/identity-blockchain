package de.iosl.blockchain.identity.discovery.registry.validator;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Component
public class RegistryEntryValidator {

	private EthereumSigner algorithm = new EthereumSigner();

	public boolean isValid(@NonNull RegistryEntry registryEntry) {
		String extractedAddress = ethAddressFromSignature(registryEntry.getPayload(), registryEntry.getSignature());
		return extractedAddress.equals(registryEntry.getPayload().getEthID());
	}

	private String ethAddressFromSignature(@NonNull Payload payload, @NonNull ECSignature signature) {
		Sign.SignatureData signatureData = signature.toSignatureData();

		BigInteger publicKey = algorithm.verifySignature(payload, signatureData);
		return addressFromPublicKey(publicKey);
	}

	public String addressFromPublicKey(BigInteger publicKey) {
		return Numeric.prependHexPrefix(Keys.getAddress(publicKey));
	}
}
