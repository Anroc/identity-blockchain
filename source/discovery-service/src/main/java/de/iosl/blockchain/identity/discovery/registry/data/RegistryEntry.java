package de.iosl.blockchain.identity.discovery.registry.data;

import com.couchbase.client.java.repository.annotation.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class RegistryEntry extends RegistryEntryDTO {

    @Id
    private String id;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date lastModified;

    @Version
    private Long version;

    public RegistryEntry(@NonNull Payload payload,
            @NonNull ECSignature signature, @NonNull String id) {
        super(payload, signature);
        this.id = id;
    }
}
