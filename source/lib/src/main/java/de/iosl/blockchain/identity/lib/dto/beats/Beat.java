package de.iosl.blockchain.identity.lib.dto.beats;

import de.iosl.blockchain.identity.lib.dto.ECSignature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beat implements Comparable<Beat>{

    @NotBlank
    private String id;
    @Min(0)
    long messageNumber;
    @NotNull
    private Date createdAt;
    @NotNull
    private ECSignature signature;
    @Valid
    @NotNull
    private HeartBeatRequest payload;

    @Override
    public int compareTo(Beat other) {
        return Long.compare(this.messageNumber, other.getMessageNumber());
    }
}
