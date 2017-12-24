package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ApiRequest;
import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class APIClientService {

    @Autowired
    private KeyChain keyChain;
    @Autowired
    private APIClient apiClient;

    private EthereumSigner ethereumSigner = new EthereumSigner();

    public List<UserClaim> getClaims() {
        if(! keyChain.isActive()) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED);
        }

        ECKeyPair ecKeyPair = new ECKeyPair(keyChain.getAccount().getPrivateKey(), keyChain.getAccount().getPublicKey());

        ApiRequest<String> claimRequest = new ApiRequest<>(
                keyChain.getAccount().getAddress(),
                ECSignature.fromSignatureData(ethereumSigner.sign(keyChain.getAccount().getAddress(), ecKeyPair))
        );

        return apiClient.getClaims(claimRequest).stream().map(UserClaim::new).collect(Collectors.toList());
    }
}
