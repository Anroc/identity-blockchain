package de.iosl.blockchain.identity.discovery.registry.validator;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
public class RegistryEntryDTOValidator {

    private EthereumSigner algorithm = new EthereumSigner();

    public boolean isValid(@NonNull RegistryEntryDTO registryEntry) {
        return isSignatureValid(registryEntry.getPayload(),
                registryEntry.getSignature(),
                registryEntry.getPayload().getEthID());
    }

    private boolean isSignatureValid(@NonNull Payload payload,
            @NonNull ECSignature signature, @NonNull String address) {
        Sign.SignatureData signatureData = signature.toSignatureData();
        return algorithm.verifySignature(payload, signatureData, address);
    }
}
