package de.iosl.blockchain.identity.core.provider.user.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
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
    private String registerContractAddress;

    @Field
    private List<PermissionGrand> permissionGrands = new ArrayList<>();

    @Field
    @NonNull
    private Set<ProviderClaim> claims;

    public User putClaim(@NonNull ProviderClaim claim) {
        claims.removeIf(c -> c.getId().equals(claim.getId()));
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

    public User addPermissionGrant(@NonNull PermissionGrand permissionGrand) {
        permissionGrands.add(permissionGrand);
        return this;
    }

    public User putPermissionGrant(@NonNull PermissionGrand permissionGrand) {
        permissionGrands.removeIf(pg -> pg.getPermissionContractAddress().equals(permissionGrand.getPermissionContractAddress()));
        return addPermissionGrant(permissionGrand);
    }

    public Optional<PermissionGrand> findPermissionGrand(@NonNull String smartContractAddress) {
        return permissionGrands.stream().filter(
                permissionGrand -> permissionGrand.getPermissionContractAddress().equals(smartContractAddress)
        ).findAny();
    }
}
