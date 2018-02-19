package de.iosl.blockchain.identity.core.shared.claims.data;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Document
public class Payload {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @NotNull
    @Field
    private ValueHolder payload;

    @NotNull
    @Field
    private ClaimType payloadType;

    public Payload(ValueHolder payload, ClaimType payloadType) {
        setPayloadType(payloadType);
        setPayload(payload);
    }

    @JsonIgnore
    public <T> T getPayload(Class<T> clazz) {
        return (T) getPayload().getUnifiedValue();
    }

    @JsonIgnore
    public String getPayloadAsString() {
        checkType(String.class, getPayloadType());
        return getPayload(String.class);
    }

    @JsonIgnore
    public double getPayloadAsDouble() {
        checkType(Double.class, getPayloadType());
        return getPayload(Double.class);
    }

    @JsonIgnore
    public LocalDateTime getPayloadAsDate() {
        checkType(LocalDateTime.class, getPayloadType());
        return getPayload(LocalDateTime.class);
    }

    @JsonIgnore
    public boolean getPayloadAsBoolean() {
        checkType(Boolean.class, getPayloadType());
        return getPayload(Boolean.class);
    }

    @JsonIgnore
    private void checkType(Class clazz, ClaimType claimType) {
        if(! payloadType.getClazz().equals(clazz)) {
            throw new IllegalArgumentException(
                    String.format("Wrong claim type. Expected [%s] but was [%s]", clazz.getSimpleName(), claimType)
            );
        }
    }

}
