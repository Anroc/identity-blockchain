package de.iosl.blockchain.identity.core.user.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class PermissionRequest {

    @Id
    @Field
    private String id;
    @Field
    @NotBlank
    private String requestingProvider;
    @Field
    @NotBlank
    private String issuedProvider;
    @Field
    @NotBlank
    private String permissionContractAddress;
    @Field
    private Map<String, Boolean> requiredClaims;
    @Field
    private Map<String, Boolean> optionalClaims;
    @Field
    private Set<ClosureRequest> closureRequests;
}
