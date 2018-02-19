package de.iosl.blockchain.identity.core.shared.ds.beats;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonLongDocument;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.dto.beats.HeartBeatRequest;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import feign.FeignException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HeartBeatService {

    private static final long RATE = 15_000L; // Milliseconds
    private static final long INITIAL_DELAY = 30_000L; // Milliseconds
    private static final String COUNTER_POST_FIX = "_beatCounter";

    @Getter
    private final EthereumSigner signer;

    @Autowired
    private HeartBeatAdapter heartBeatAdapter;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private BlockchainIdentityConfig config;
    @Autowired
    private Bucket bucket;

    @Getter
    private Queue<EventListener> eventListeners;

    public HeartBeatService() {
        this.signer = new EthereumSigner();
        this.eventListeners = new ConcurrentLinkedQueue<>();
    }

    public Optional<RegistryEntryDTO> discover(@NonNull String ethID) {
        RequestDTO<RegistryEntryDTO> requestDTO = heartBeatAdapter.discover(ethID);
        if(signer.verifySignature(
                requestDTO.getPayload(),
                requestDTO.getSignature().toSignatureData(),
                requestDTO.getPayload().getEthID())
                && requestDTO.getPayload().getEthID().equals(ethID)) {

            log.info("Trusted register entry at {}.", ethID);
            return Optional.of(requestDTO.getPayload());
        } else {
            log.warn("Untrusted register entry at {}.", ethID);
            return Optional.empty();
        }
    }

    public void subscribe(@NonNull EventListener eventListener) {
        eventListeners.add(eventListener);
    }

    public Beat createURLBeat(@NonNull String ethID, @NonNull EventType eventType) {
        return createBeat(ethID, eventType, config.getHostUrl(), SubjectType.URL);
    }

    public Beat createEthIdBeat(@NonNull String ethID, @NonNull EventType eventType, @NonNull String subjectEthId) {
        return createBeat(ethID, eventType, subjectEthId, SubjectType.ETHEREUM_ADDRESS);
    }

    private Beat createBeat(@NonNull String ethId, @NonNull EventType eventType, @NonNull String subject, @NonNull SubjectType subjectType) {
        if(! keyChain.isActive()) {
            throw new ServiceException("Keychain is not unlocked.", HttpStatus.UNAUTHORIZED);
        }

        HeartBeatRequest heartBeatRequest = new HeartBeatRequest(
                keyChain.getAccount().getAddress(),
                subject,
                eventType,
                subjectType
        );

        ECKeyPair ecKeyPair = ECKeyPair.create(keyChain.getAccount().getPrivateKey());
        RequestDTO<HeartBeatRequest> heartBeatRequestRequestDTO = new RequestDTO<>(
                heartBeatRequest,
                ECSignature.fromSignatureData(signer.sign(heartBeatRequest, ecKeyPair))
        );

        log.info("Creating beat for {} with event {} for subject {} and subjectType", ethId, eventType, subject, subjectType);
        return heartBeatAdapter.createBeat(ethId, heartBeatRequestRequestDTO);
    }

    @Scheduled(fixedRate = RATE, initialDelay = INITIAL_DELAY)
    public void beat() {
        if(! keyChain.isRegistered()) {
            log.debug("Not yet registered.");
            return;
        }

        long beatCounter = findBeatCounter().orElse(0L);

        try {
            log.debug("Sending beat as [{}] from {} to {}", keyChain.getAccount().getAddress(), beatCounter, Long.MAX_VALUE);
            List<Beat> beats = heartBeatAdapter.beat(keyChain.getAccount().getAddress(), beatCounter, Long.MAX_VALUE);

            if(! beats.isEmpty()) {
                beats = beats.stream()
                        .filter(beat -> {
                            if(getSigner().verifySignature(
                                    beat.getPayload(),
                                    beat.getSignature().toSignatureData(),
                                    beat.getPayload().getEthID())) {
                                log.info("New beat from [{}]", beat.getPayload().getEthID());
                                return true;
                            } else {
                                log.warn("Untrusted (invalid) new beat from [{}]. Ignored.", beat.getPayload().getEthID());
                                return false;
                            }
                        })
                        .sorted()
                        .collect(Collectors.toList());

                beatCounter = beats.get(beats.size() -1).getMessageNumber() + 1;
                persistBeatCounter(beatCounter);
                beats.forEach(
                        beat -> getEventListeners().forEach(
                                eventListener -> eventListener.trigger(new Event(beat), beat.getPayload().getEventType())
                        )
                );
            }
        } catch (FeignException e) {
            log.error("Could not process beat!", e);
        }
    }

    protected Optional<Long> findBeatCounter() {
        return Optional.ofNullable(bucket.get(JsonLongDocument.create(buildCounterId()))).map(JsonLongDocument::content);
    }

    protected void persistBeatCounter(long beatCounter) {
        bucket.upsert(JsonLongDocument.create(buildCounterId(), beatCounter));
    }

    private String buildCounterId() {
        return keyChain.getAccount().getAddress() + COUNTER_POST_FIX;
    }
}
