package de.iosl.blockchain.identity.core.shared.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Claim {

    @Id
    @NonNull
    @Field
    private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Valid
    @NonNull
    private Payload payload;

    @Valid
    @NonNull
    private Provider provider;
}
