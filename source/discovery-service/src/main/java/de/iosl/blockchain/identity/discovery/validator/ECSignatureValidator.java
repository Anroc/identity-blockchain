package de.iosl.blockchain.identity.discovery.validator;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

@Component
@Slf4j
public class ECSignatureValidator {

    private EthereumSigner algorithm = new EthereumSigner();

    public boolean isValid(@NonNull RequestDTO<?> request) {
        return isSignatureValid(request.getPayload(),
                request.getSignature(),
                request.getPayload().getEthID());
    }

    private boolean isSignatureValid(@NonNull Object payload, @NonNull ECSignature signature, @NonNull String address) {
        Sign.SignatureData signatureData = signature.toSignatureData();
        return algorithm.verifySignature(payload, signatureData, address);
    }
}
