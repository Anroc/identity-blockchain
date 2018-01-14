package de.iosl.blockchain.identity.core.user.messages.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id;
    @Field
    private MessageType messageType;
    @Field
    private boolean seen;
    @Field
    @CreatedDate
    private Date creationDate;

    public Message(String id, MessageType messageType, boolean seen) {
        this.id = id;
        this.messageType = messageType;
        this.seen = seen;
    }
}
