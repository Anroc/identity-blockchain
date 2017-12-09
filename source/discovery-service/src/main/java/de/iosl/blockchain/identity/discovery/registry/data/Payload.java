package de.iosl.blockchain.identity.discovery.registry.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Payload {
    @Field
    private String ethID;
    @Field
    private String publicKey;
    @Field
    private String domainName;
    @Field
    private int port;
}
