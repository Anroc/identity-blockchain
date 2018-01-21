package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientBeanFactory;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientRegistry;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.repository.UserClaimDB;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class APIClientService {

    @Autowired
    private KeyChain keyChain;
    @Autowired
    private UserClaimDB userClaimDB;
    @Autowired
    private HeartBeatService heartBeatService;

    private final EthereumSigner ethereumSigner;
    private final APIClientRegistry<UserAPIClient> apiClientRegistry;

    @Autowired
    public APIClientService(APIClientBeanFactory apiClientBeanFactory) {
        this.ethereumSigner = new EthereumSigner();
        this.apiClientRegistry = new APIClientRegistry<>(apiClientBeanFactory, UserAPIClient.class);
    }

    @PostConstruct
    public void createSubscriber() {
        heartBeatService.subscribe(
                (event, eventType) -> {
                    switch (eventType) {
                        case NEW_CLAIMS:
                            apiClientRegistry.register(event.getSubject());
                            getAndSaveClaims(event.getSubject());
                            break;
                    }
                }
        );
    }

    private List<UserClaim> requestGetClaims(String url) {
        if(! keyChain.isActive()) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED);
        }

        UserAPIClient userApiClient = apiClientRegistry.getOrRegister(url);

        ECKeyPair ecKeyPair = new ECKeyPair(keyChain.getAccount().getPrivateKey(), keyChain.getAccount().getPublicKey());

        SignedRequest<BasicEthereumDTO> claimRequest = new SignedRequest<>(
                new BasicEthereumDTO(keyChain.getAccount().getAddress()),
                ECSignature.fromSignatureData(ethereumSigner.sign(keyChain.getAccount().getAddress(), ecKeyPair))
        );


        return userApiClient.getClaims(claimRequest)
                .stream()
                .map(claimDTO -> new UserClaim(claimDTO, keyChain.getAccount().getAddress()))
                .collect(Collectors.toList());
    }

    public List<UserClaim> getAndSaveClaims(@NonNull String url) {
        List<UserClaim> claims = requestGetClaims(url);
        claims.forEach(userClaimDB::upsert);
        return claims;
    }
}
