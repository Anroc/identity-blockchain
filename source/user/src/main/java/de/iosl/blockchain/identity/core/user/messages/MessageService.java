package de.iosl.blockchain.identity.core.user.messages;

import de.iosl.blockchain.identity.core.user.messages.data.Message;
import de.iosl.blockchain.identity.core.user.messages.data.MessageType;
import de.iosl.blockchain.identity.core.user.messages.db.MessageDB;
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

    public Message createMessage(@NonNull MessageType messageType) {
        Message message = new Message(UUID.randomUUID().toString(), messageType, false);
        messageDB.insert(message);
        return message;
    }

    public Message updateMessage(@NonNull Message message, @NonNull boolean seen) {
        message.setSeen(seen);
        messageDB.update(message);
        return message;
    }

    public Optional<Message> findMessage(@NonNull String id) {
        return messageDB.findEntity(id);
    }
}
