package de.iosl.blockchain.identity.core.provider.data.user;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

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
    @NotBlank
    private String publicKey;

    @Field
    @NotBlank
    private String ethId;

    @Field
    @NotBlank
    private ArrayList<Claim> claimList;
}
