package de.iosl.blockchain.identity.core.user.messages;

import de.iosl.blockchain.identity.core.user.messages.dto.MessageDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/messages")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    @ApiOperation("Get messages")
    public List<MessageDTO> getMessages(
            @RequestParam(value = "includeSeen", defaultValue = "false") boolean includeSeen) {
        return messageService.getMessages(includeSeen)
                .stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }
}
