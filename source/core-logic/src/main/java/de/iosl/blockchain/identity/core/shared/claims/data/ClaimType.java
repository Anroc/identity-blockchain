package de.iosl.blockchain.identity.core.shared.claims.data;

import static de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperations.*;

public enum ClaimType {

    STRING(EQ, NEQ),
    NUMBER(EQ, NEQ, GT, GE, LT, LE),
    OBJECT(),
    DATE(EQ, NEQ, GT, GE, LT, LE),
    BOOLEAN(EQ, NEQ);

    private final ClaimOperations[] supportedClaimOperation;

    ClaimType(ClaimOperations... claimOperations) {
        this.supportedClaimOperation = claimOperations;
    }
}
