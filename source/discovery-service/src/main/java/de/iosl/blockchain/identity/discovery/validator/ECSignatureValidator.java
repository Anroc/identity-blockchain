package de.iosl.blockchain.identity.discovery.validator;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.data.ECSignature;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
public class ECSignatureValidator {

    private EthereumSigner algorithm = new EthereumSigner();

    public boolean isValid(@NonNull RequestDTO<?> registryEntry) {
        return isSignatureValid(registryEntry.getPayload(),
                registryEntry.getSignature(),
                registryEntry.getPayload().getEthID());
    }

    private boolean isSignatureValid(@NonNull Object payload,
            @NonNull ECSignature signature, @NonNull String address) {
        Sign.SignatureData signatureData = signature.toSignatureData();
        return algorithm.verifySignature(payload, signatureData, address);
    }
}
