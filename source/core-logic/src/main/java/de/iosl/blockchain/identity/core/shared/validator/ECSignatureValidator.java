package de.iosl.blockchain.identity.core.shared.validator;

import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
public class ECSignatureValidator {

    private EthereumSigner algorithm = new EthereumSigner();

    public boolean isValid(@NonNull SignedRequest<?> signedRequest, @NonNull String ethID) {
        return isSignatureValid(signedRequest.getPayload(),
                signedRequest.getSignature(),
                ethID);
    }

    public boolean isRequestValid(@NonNull SignedRequest<?> signedRequest) {
        return isSignatureValid(signedRequest.getPayload(), signedRequest.getSignature(), signedRequest.getEthID());
    }

    public boolean isSignatureValid(@NonNull Object payload, @NonNull ECSignature signature, @NonNull String address) {
        Sign.SignatureData signatureData = signature.toSignatureData();
        return algorithm.verifySignature(payload, signatureData, address);
    }
}
