package de.iosl.blockchain.identity.core.shared.message.db;

import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDB extends CouchbaseWrapper<Message, String> {

    private final MessageRepository repository;

    @Autowired
    public MessageDB(MessageRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Message> findMessagesBySeen(boolean seen) {
        return repository.findAllBySeen(seen);
    }

}
