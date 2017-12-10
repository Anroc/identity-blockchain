package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id;
    @Field
    long messageNumber;
    @Field
    private String senderEthID;
    @Field
    private String endpoint;

    @Field
    @CreatedDate
    private Date createdAt;

    public Message(long messageNumber, String recepiantEthID, String senderEthID, String endpoint) {
        this.id = buildID(recepiantEthID, messageNumber);
        this.messageNumber = messageNumber;
        this.senderEthID = senderEthID;
        this.endpoint = endpoint;
    }

    public static String buildID(@NonNull String ethID, @NonNull long messageNumber) {
        return ethID + "_" + messageNumber;
    }
}
