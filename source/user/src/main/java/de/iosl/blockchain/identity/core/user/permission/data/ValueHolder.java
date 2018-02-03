package de.iosl.blockchain.identity.core.user.permission.data;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This class is needed so that LocalDateTime which get to maped into instance from epoch time
 * gets not deserialize. So we need to explicit cast this one into a timeValue to ensure
 * correct mapping.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueHolder {

    @Field
    private Object value;
    @Field
    private LocalDateTime timeValue;

    public ValueHolder(Object value) {
        if(value instanceof LocalDateTime) {
            this.timeValue = (LocalDateTime) value;
        } else {
            this.value = value;
        }
    }

    @JsonIgnore
    public Object getUnifiedValue() {
        return value != null? value : timeValue;
    }
}
