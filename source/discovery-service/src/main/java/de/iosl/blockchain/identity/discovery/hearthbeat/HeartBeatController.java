package de.iosl.blockchain.identity.discovery.hearthbeat;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.HeartBeatInfoDTO;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.HeartBeatRequest;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.LifeState;
import de.iosl.blockchain.identity.discovery.registry.DiscoveryService;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.discovery.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/heartbeat")
public class HeartBeatController {

    @Autowired
    private ECSignatureValidator validator;
    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private DiscoveryService discoveryService;

    @GetMapping("/{ethID}")
    public List<Beat> heartBeat(@NotBlank @PathVariable("ethID") String ethID,
            @RequestParam(value = "from", defaultValue = "0") long from,
            @RequestParam(value = "to", defaultValue = "9223372036854775807") long to) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.debug("Received beat from [{}]", ethID);
        List<Beat> beats = heartBeatService.heartBeat(ethID, from, to);
        stopWatch.stop();
        log.debug("Executed lifecycle in {}ms", stopWatch.getLastTaskTimeMillis());
        return beats;
    }

    @PostMapping("/{ethID}")
    public Beat createBeat(@NotBlank @PathVariable("ethID") String ethID,
            @Valid @RequestBody RequestDTO<HeartBeatRequest> messageRequest) {
        log.info("New beat received.");
        if (! validator.isValid(messageRequest)) {
            log.info("Signature was invalid.");
            try {
                log.info("This is the object {}", new ObjectMapper().writeValueAsString(messageRequest));
            } catch (JsonProcessingException e) {
                throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }
        log.info("Created beat for [{}] from [{}]", ethID, messageRequest.getPayload().getEthID());

        return heartBeatService.createBeat(messageRequest, ethID);
    }

    @GetMapping
    public List<HeartBeatInfoDTO> getInfo() {
        return discoveryService.getEntries().stream()
                .map(registryEntry -> {
                    LifeState lifeState = LifeState.from(registryEntry.getLastSeen());
                    RequestDTO<RegistryEntryDTO> dto = registryEntry.toDTO();
                    return new HeartBeatInfoDTO(dto.getPayload(), dto.getSignature(), lifeState, registryEntry.getLastSeen());
                }).collect(Collectors.toList());
    }
}
