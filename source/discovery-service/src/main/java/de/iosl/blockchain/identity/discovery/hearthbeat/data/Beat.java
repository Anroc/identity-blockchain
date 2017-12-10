package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.discovery.data.ECSignature;
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
public class Beat {

    @Id
    private String id;
    @Field
    long messageNumber;
    @CreatedDate
    private Date createdAt;
    @Field
    private ECSignature signature;
    @Field
    private BeatContent payload;

    public Beat(long messageNumber, String recipientEthID, String senderEthID, String endpoint, ECSignature signature) {
        this.id = buildID(recipientEthID, messageNumber);
        this.messageNumber = messageNumber;

        this.payload = new BeatContent(senderEthID, endpoint);
        this.signature = signature;
    }

    public static String buildID(@NonNull String ethID, long messageNumber) {
        return ethID + "_" + messageNumber;
    }
}
