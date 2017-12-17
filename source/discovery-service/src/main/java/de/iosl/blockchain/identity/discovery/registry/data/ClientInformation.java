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
public class ClientInformation {
    @Field
    private String ethID;
    @Field
    private String publicKey;
    @Field
    private String domainName;
    @Field
    private int port;

    public ClientInformation(RegistryEntryDTO registryEntryDTO) {
        this.ethID = registryEntryDTO.getEthID();
        this.publicKey = registryEntryDTO.getPublicKey();
        this.domainName = registryEntryDTO.getDomainName();
        this.port = registryEntryDTO.getPort();
    }

    public RegistryEntryDTO toRegisterEntryDTO() {
        return new RegistryEntryDTO(
                getEthID(),
                getPublicKey(),
                getDomainName(),
                getPort()
        );
    }
}
