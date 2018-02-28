package de.iosl.blockchain.identity.core.shared.message.dto;

import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private String id;
    private MessageType messageType;
    private String userId;
    private Date creationDate;
    private boolean seen;
    private String subjectID;

    public MessageDTO(@NonNull Message message) {
        this.id = message.getId();
        this.messageType = message.getMessageType();
        this.seen = message.isSeen();
        this.creationDate = message.getCreationDate();
        this.userId = message.getUserId();
        this.subjectID = message.getSubjectID();
    }
}
