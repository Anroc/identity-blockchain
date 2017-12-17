package de.iosl.blockchain.identity.core.shared.ds.beats.data;

import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beat implements Comparable<Beat>{
    private String id;
    long messageNumber;
    private Date createdAt;
    private ECSignature signature;
    private HeartBeatRequest payload;

    @Override
    public int compareTo(Beat other) {
        return Long.compare(this.messageNumber, other.getMessageNumber());
    }
}
