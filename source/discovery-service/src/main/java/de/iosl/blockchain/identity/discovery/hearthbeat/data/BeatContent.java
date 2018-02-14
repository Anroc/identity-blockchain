package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true, value = {"ethID", "subject", "subjectType", "eventType"})
public class BeatContent {

    @Field
    private String ethID;
    @Field
    private String subject;
    @Field
    private SubjectType subjectType;
    @Field
    private EventType eventType;

}
