package de.iosl.blockchain.identity.core.shared.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
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
    @NotBlank
    @Field
    private String id;

    @LastModifiedDate
    private Date modificationDate;

    @CreatedDate
    private Date creationDate;

    @NotBlank
    @Valid
    private Provider provider;

    @NotBlank
    @Valid
    private Payload claimValue;

}
