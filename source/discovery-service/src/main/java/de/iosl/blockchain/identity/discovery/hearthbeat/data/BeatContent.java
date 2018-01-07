package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeatContent {

    @Field
    private String ethID;
    @Field
    private String url;
    @Field
    private EventType eventType;

}
