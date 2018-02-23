package de.iosl.blockchain.identity.core.user.factories;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;

public class PayloadFactory {

    public Payload create(Object content, ClaimType type) {
        return new Payload(new ValueHolder(content), type);
    }

    public Payload create() {
        return new Payload(new ValueHolder("content"), ClaimType.STRING);
    }

    public static PayloadFactory instance() {
        return new PayloadFactory();
    }
}
