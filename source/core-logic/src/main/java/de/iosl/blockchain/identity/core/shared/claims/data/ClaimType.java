package de.iosl.blockchain.identity.core.shared.claims.data;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Arrays;

import static de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation.*;

@Getter
public enum ClaimType {

    STRING(String.class, EQ, NEQ),
    NUMBER(Double.class, EQ, NEQ, GT, GE, LT, LE),
    OBJECT(Object.class),
    DATE(LocalDateTime.class, EQ, NEQ, GT, GE, LT, LE),
    BOOLEAN(Boolean.class, EQ, NEQ);

    private final ClaimOperation[] supportedClaimOperation;
    private final Class<?> clazz;

    ClaimType(Class<?> clazz, ClaimOperation... claimOperations) {
        this.supportedClaimOperation = claimOperations;
        this.clazz = clazz;
    }

    public boolean supports(@NonNull ClaimOperation claimOperation) {
        return Arrays.stream(supportedClaimOperation)
                .anyMatch(supportedClaimOperation -> supportedClaimOperation == claimOperation);
    }

    public boolean validateType(@NonNull Object value) {
        switch (this) {
            case DATE:
                return value instanceof LocalDateTime;
            case NUMBER:
                return value instanceof Double;
            case STRING:
                return value instanceof String;
            case BOOLEAN:
                return value instanceof Boolean;
            case OBJECT:
            default:
                return true;
        }
    }
}
