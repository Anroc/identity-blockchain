package de.iosl.blockchain.identity.core.shared.claims.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Provider {

    @Field
    private String ethID;

    @Field
    @NotBlank
    private String name;

}
