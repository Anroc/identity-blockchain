package de.iosl.blockchain.identity.core.shared.message.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Date;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id;
    @Field
    private MessageType messageType;
    @Field
    private String userId;
    @Field
    private boolean seen;
    @Field
    private String subjectID;
    @Field
    @CreatedDate
    private Date creationDate;

    public Message(String id, MessageType messageType, boolean seen, String subjectID) {
        this.id = id;
        this.messageType = messageType;
        this.seen = seen;
        this.subjectID = subjectID;
    }
}
