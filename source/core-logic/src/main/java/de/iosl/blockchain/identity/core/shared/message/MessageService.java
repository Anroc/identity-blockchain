package de.iosl.blockchain.identity.core.shared.message;

import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.data.MessageType;
import de.iosl.blockchain.identity.core.shared.message.db.MessageDB;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessageDB messageDB;

    public List<Message> getMessages(boolean includeSeen) {
        return messageDB.findMessagesBySeen(includeSeen);
    }

    public Message createMessage(@NonNull MessageType messageType, String subjectID) {
        Message message = new Message(UUID.randomUUID().toString(), messageType, false, subjectID);
        messageDB.insert(message);
        return message;
    }

    public Message createMessage(@NonNull MessageType messageType, @NonNull String userId, String subjectID) {
        Message message = new Message(UUID.randomUUID().toString(), messageType, false, subjectID);
        message.setUserId(userId);
        messageDB.insert(message);
        return message;
    }

    public Message updateMessage(@NonNull Message message, boolean seen) {
        message.setSeen(seen);
        messageDB.update(message);
        return message;
    }

    public Optional<Message> findMessage(@NonNull String id) {
        return messageDB.findEntity(id);
    }
}
