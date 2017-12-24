package de.iosl.blockchain.identity.core.provider.validator;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
public class ECSignatureValidator {

    private EthereumSigner algorithm = new EthereumSigner();

    public boolean isValid(@NonNull ApiRequest<?> apiRequest, @NonNull String ethID) {
        return isSignatureValid(apiRequest.getPayload(),
                apiRequest.getSignature(),
                ethID);
    }

    public boolean isGetRequestValid(@NonNull ApiRequest<String> ethIDRequest) {
        return isSignatureValid(ethIDRequest.getPayload(), ethIDRequest.getSignature(), ethIDRequest.getPayload());
    }

    private boolean isSignatureValid(@NonNull Object payload,
            @NonNull ECSignature signature, @NonNull String address) {
        Sign.SignatureData signatureData = signature.toSignatureData();
        return algorithm.verifySignature(payload, signatureData, address);
    }
}
