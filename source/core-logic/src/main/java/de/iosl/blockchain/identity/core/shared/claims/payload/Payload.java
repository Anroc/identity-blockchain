package de.iosl.blockchain.identity.core.shared.claims.payload;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.Valid;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Payload {

    public enum PayloadType{
        STRING, NUMBER, OBJECT, DATE, BOOLEAN;
    }

    @NotBlank
    @Field
    private Object payload;

    @NotBlank
    @Field
    private PayloadType payloadType;

}
