package de.iosl.blockchain.identity.core.user.messages;

import de.iosl.blockchain.identity.core.user.messages.data.Message;
import de.iosl.blockchain.identity.core.user.messages.dto.MessageDTO;
import de.iosl.blockchain.identity.core.user.messages.dto.MessageUpdateDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @PutMapping("/{id}")
    @ApiOperation("Updates a message status")
    public MessageDTO updateMessage(
            @PathVariable("id") @NotBlank String id,
            @NotNull @Valid @RequestBody MessageUpdateDTO updatedMessage) {

        Message message = messageService.findMessage(id)
                .orElseThrow(
                        () -> new ServiceException("Message [%s] could not be found.", HttpStatus.NOT_FOUND, id)
                );

        return new MessageDTO(messageService.updateMessage(message, updatedMessage.isSeen()));
    }
}
