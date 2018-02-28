package de.iosl.blockchain.identity.core.user.permission;

import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PermissionRequestListener {

    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private PermissionService permissionService;

    @PostConstruct
    public void listenForPermissionContracts() {
        heartBeatService.subscribe(
                (event, eventType) -> {
                    if(eventType == EventType.NEW_PPR) {
                        if (event.getSubjectType() != SubjectType.ETHEREUM_ADDRESS) {
                            throw new IllegalStateException("Event NEW_PPR received but was not from type EtheremAddr");
                        }

                        log.info("Received new beat for NEW_PPR.");
                        permissionService.handleNewPermissionRequest(event.getEthID(), event.getSubject());
                    }
                }
        );
    }
}
