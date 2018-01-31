package de.iosl.blockchain.identity.core.shared.claims.data;

import lombok.Getter;

@Getter
public enum ClaimOperation {

    EQ("equals"),
    NEQ("not equals"),
    LT("less then"),
    LE("less or equals"),
    GT("greater then"),
    GE("greater or equals");

    private final String description;

    ClaimOperation(String description) {
        this.description = description;
    }
}
