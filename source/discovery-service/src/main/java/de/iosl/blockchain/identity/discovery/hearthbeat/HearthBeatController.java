package de.iosl.blockchain.identity.discovery.hearthbeat;

import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Message;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.MessageRequest;
import de.iosl.blockchain.identity.discovery.hearthbeat.db.MessageDB;
import de.iosl.blockchain.identity.discovery.registry.db.RegistryEntryDB;
import de.iosl.blockchain.identity.discovery.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/heartbeat")
public class HearthBeatController {

    @Autowired
    private RegistryEntryDB registryEntryDB;
    @Autowired
    private MessageDB messageDB;
    @Autowired
    private ECSignatureValidator validator;

    @GetMapping("/{ethID}")
    public List<Message> hearthBeat(@NotBlank @PathVariable("ethID") String ethID,
            @RequestParam(value = "from", defaultValue = "0") long from,
            @RequestParam(value = "to", defaultValue = "9223372036854775807") long to) {

        registryEntryDB.updateLastSeen(ethID, new Date());
        long counter = messageDB.getCounter(ethID);

        if (from < counter) {
            if( to >= Long.MAX_VALUE) {
                to = counter;
            }

            return messageDB.findMessagesByEthIDAndCounterRange(ethID, from, to);
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/{ethID}")
    public Message createMessage(@NotBlank @PathVariable("ethID") String ethID,
            @Valid @RequestBody RequestDTO<MessageRequest> messageRequest) {

        if (! validator.isValid(messageRequest)) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        long counter = messageDB.getCounter(ethID); // init on non exist
        messageDB.incrementCounter(ethID);

        Message message = new Message(
                counter,
                ethID,
                messageRequest.getPayload().getEthID(),
                messageRequest.getPayload().getEndpoint()
        );
        messageDB.insert(message);
        return message;
    }
}
