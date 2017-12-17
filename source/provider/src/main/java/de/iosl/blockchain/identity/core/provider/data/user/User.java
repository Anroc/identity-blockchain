package de.iosl.blockchain.identity.core.provider.data.user;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.provider.data.claim.ProviderClaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Optional;
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
    private Set<ProviderClaim> claims;

    public User putClaim(@NonNull ProviderClaim claim) {
        claims.add(claim);
        return this;
    }

    public User removeClaim(String claimId) {
        findClaim(claimId).ifPresent(claims::remove);
        return this;
    }

    public Optional<ProviderClaim> findClaim(final String claimId) {
        return claims.stream()
                .filter(claim -> claim.getId().equals(claimId))
                .findAny();
    }
}
