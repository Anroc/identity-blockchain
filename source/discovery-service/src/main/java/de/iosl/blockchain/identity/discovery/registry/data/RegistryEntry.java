package de.iosl.blockchain.identity.discovery.registry.data;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class RegistryEntry {

    @Id
    private String id;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date lastModified;

    @Version
    private Long version;

    @Field
    private Date lastSeen;

    @Field
    private ClientInformation clientInformation;

    @Field
    private ECSignature ecSignature;

    public RegistryEntry(@NonNull RegistryEntryDTO payload,
            @NonNull ECSignature signature, @NonNull String id) {
        this.clientInformation = new ClientInformation(payload);
        this.ecSignature = signature;
        this.id = id;
        this.lastSeen = new Date();
    }

    public RequestDTO<RegistryEntryDTO> toDTO() {
        return new RequestDTO<>(
                this.getClientInformation().toRegisterEntryDTO(),
                this.getEcSignature()
        );
    }
}
