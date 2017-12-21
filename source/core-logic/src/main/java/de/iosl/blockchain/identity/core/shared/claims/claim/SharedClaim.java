package de.iosl.blockchain.identity.core.shared.claims.claim;

import com.couchbase.client.java.repository.annotation.Field;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document
@AllArgsConstructor
public abstract class SharedClaim {

    @NotNull
    @Field
    private Date modificationDate;

    @NotNull
    @Valid
    private Provider provider;

    @NotNull
    @Valid
    private Payload claimValue;

    public abstract String getId();

}
