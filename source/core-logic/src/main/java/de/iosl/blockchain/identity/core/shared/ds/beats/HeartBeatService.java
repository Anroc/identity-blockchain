package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.Beat;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import feign.FeignException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HeartBeatService {

    private static final long RATE = 15_000L; // Milliseconds
    private static final long INITIAL_DELAY = 30_000L; // Milliseconds

    @Getter
    private final EthereumSigner signer;

    @Autowired
    private HeartBeatAdapter heartBeatAdapter;
    @Autowired
    private KeyChain keyChain;

    @Setter
    @Getter
    private long beatCounter = 0L;

    private Queue<EventListener> eventListeners;

    public HeartBeatService() {
        this.signer = new EthereumSigner();
        this.eventListeners = new ConcurrentLinkedQueue<>();
    }

    public void subscribe(@NonNull EventListener eventListener) {
        eventListeners.add(eventListener);
    }

    @Scheduled(fixedRate = RATE, initialDelay = INITIAL_DELAY)
    public void beat() {
        if(! keyChain.isRegistered()) {
            log.debug("Not yet registered.");
            return;
        }

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
                beats.forEach(
                        beat -> eventListeners.forEach(
                                eventListener -> eventListener.trigger(new Event(beat), beat.getPayload().getEventType())
                        )
                );
            }
        } catch (FeignException e) {
            log.error("Could not send beat!", e);
        }
    }
}
