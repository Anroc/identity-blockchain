package de.iosl.blockchain.identity.core.shared.claims.data;

import lombok.Getter;

import java.time.LocalDateTime;

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
}
