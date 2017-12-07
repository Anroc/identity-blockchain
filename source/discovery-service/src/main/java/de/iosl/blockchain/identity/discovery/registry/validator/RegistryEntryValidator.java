package de.iosl.blockchain.identity.discovery.registry.validator;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
public class RegistryEntryValidator {

	private EthereumSigner algorithm = new EthereumSigner();

	public boolean isValid(@NonNull RegistryEntry registryEntry) {
		return isSignatureValid(registryEntry.getPayload(), registryEntry.getSignature(), registryEntry.getPayload().getEthId())
				&& isRequestApprovedByEntity(registryEntry.getPayload());
	}

	private boolean isSignatureValid(@NonNull Payload payload, @NonNull ECSignature signature, @NonNull String address) {
		Sign.SignatureData signatureData = signature.toSignatureData();
		return algorithm.verifySignature(payload, signatureData, address);
	}

	public boolean isRequestApprovedByEntity(@NonNull Payload payload) {
		/* TODO: implement
		 * 1. Request user with given eth ID
		 * 2. Create Contract
		 * 3. wait for user approval
		 * 4. return response
		 */
		return true;
	}
}
