package de.iosl.blockchain.identity.core.shared.claims.payload;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Payload {

    public enum Type{
        STRING, NUMBER, OBJECT, DATE, BOOLEAN;
    }

    @NotNull
    @Field
    private Object payload;

    @NotNull
    @Field
    private Type payloadType;

}
