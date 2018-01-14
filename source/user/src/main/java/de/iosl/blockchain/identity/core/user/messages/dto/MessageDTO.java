package de.iosl.blockchain.identity.core.user.messages.dto;

import de.iosl.blockchain.identity.core.user.messages.data.Message;
import de.iosl.blockchain.identity.core.user.messages.data.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @NotNull
    private MessageType messageType;
    @NotNull
    private Date creationDate;

    private boolean seen;

    public MessageDTO(@NonNull Message message) {
        this.messageType = message.getMessageType();
        this.seen = message.isSeen();
        this.creationDate = message.getCreationDate();
    }
}
