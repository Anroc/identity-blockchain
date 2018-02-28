package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientBeanFactory;
import de.iosl.blockchain.identity.core.shared.api.client.APIClientRegistry;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.message.MessageService;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.core.shared.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.db.UserClaimDB;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class APIClientService {

    @Autowired
    private KeyChain keyChain;
    @Autowired
    private UserClaimDB userClaimDB;
    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private ECSignatureValidator signatureValidator;
    @Autowired
    private MessageService messageService;

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
                            if(event.getSubjectType() == SubjectType.URL) {
                                apiClientRegistry.register(event.getSubject());
                                log.info("Retrieving claims from provider...");
                                getAndSaveClaims(event.getSubject());

                                log.info("Creating new message: NEW_CLAIMS");
                                messageService.createMessage(MessageType.NEW_CLAIMS, null);
                            }
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

        BasicEthereumDTO basicEthereumDTO = new BasicEthereumDTO(keyChain.getAccount().getAddress());

        SignedRequest<BasicEthereumDTO> claimRequest = new SignedRequest<>(
                new BasicEthereumDTO(keyChain.getAccount().getAddress()),
                ECSignature.fromSignatureData(ethereumSigner.sign(basicEthereumDTO, ecKeyPair))
        );


        return userApiClient.getClaims(claimRequest)
                .stream()
                .filter(claimDTO -> validateSignedRequest(claimDTO.getSignedClaimDTO()) && validateSignedRequestCollection(claimDTO.getSignedClosures()))
                .map(claimDTO -> new UserClaim(claimDTO, keyChain.getAccount().getAddress()))
                .collect(Collectors.toList());
    }

    private boolean validateSignedRequest(SignedRequest<?> signedRequest) {
        boolean valid = signatureValidator.isRequestValid(signedRequest);

        if(! valid ) {
            log.warn("Signature for was not valid: {}", signedRequest);
        }
        return valid;
    }

    private boolean validateSignedRequestCollection(List<SignedRequest<Closure>> signedRequestCollection) {
        log.info("Validating closure...");
        if(signedRequestCollection != null) {
            return signedRequestCollection.stream().filter(this::validateSignedRequest).count()
                    == signedRequestCollection.size();
        } else {
            return true;
        }
    }

    public List<UserClaim> getAndSaveClaims(@NonNull String url) {
        List<UserClaim> claims = requestGetClaims(url);
        claims.forEach(userClaimDB::upsert);
        return claims;
    }
}
