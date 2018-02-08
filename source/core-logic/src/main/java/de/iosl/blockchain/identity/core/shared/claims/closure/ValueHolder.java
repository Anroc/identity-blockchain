package de.iosl.blockchain.identity.core.shared.claims.closure;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is needed so that LocalDateTime which get to maped into instance from epoch time
 * gets not deserialize. So we need to explicit cast this one into a timeValue to ensure
 * correct mapping.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic=true, value = {"value", "timeValue"})
public class ValueHolder implements Serializable {

    private static final long serialVersionUID = -349128374L;

    @Field
    @ApiModelProperty(
            value = "For data that are not dates."
    )
    private Object value;

    @Field
    @ApiModelProperty(
            value = "For date related input/output.",
            notes = "Will return a list of the following values: [Year, Month, DayOfMonth, Hour, Minute, Second]",
            dataType = "java.util.List"
    )
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

    @JsonIgnore
    public <T> T getUnifiedValueAs(Class<T> clazz) {
        return (T) getUnifiedValue();
    }
}
