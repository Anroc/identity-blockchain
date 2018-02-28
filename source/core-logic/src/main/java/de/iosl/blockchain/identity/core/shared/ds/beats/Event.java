package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String ethID;
    private String subject;
    private Date createdAt;
    private SubjectType subjectType;

    public Event(@NonNull Beat beat) {
        this.ethID = beat.getPayload().getEthID();
        this.subject = beat.getPayload().getSubject();
        this.subjectType = beat.getPayload().getSubjectType();
        this.createdAt = beat.getCreatedAt();
    }
}
