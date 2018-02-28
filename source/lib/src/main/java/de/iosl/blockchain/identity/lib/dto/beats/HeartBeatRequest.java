package de.iosl.blockchain.identity.lib.dto.beats;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.lib.dto.Payload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder(value = {"ethID", "eventType", "subject", "subjectType"}, alphabetic=true)
public class HeartBeatRequest extends Payload {

    @NotBlank
    private String subject;

    @NotNull
    private SubjectType subjectType;

    @NotNull
    private EventType eventType;

    public HeartBeatRequest(String ethID, String subject, EventType eventType, SubjectType subjectType) {
        super(ethID);
        this.subject = subject;
        this.eventType = eventType;
        this.subjectType = subjectType;
    }
}
