package de.iosl.blockchain.identity.core.provider.data.user;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.claims.claim.SharedClaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

    @Id
    @Field
    @NotBlank
    private String id;

    @Field
    private String publicKey;

    @Field
    private String ethId;

    @Field
    @NonNull
    private Set<SharedClaim> claims;
}
