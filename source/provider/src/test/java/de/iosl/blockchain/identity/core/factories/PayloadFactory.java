package de.iosl.blockchain.identity.core.factories;

import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.payload.PayloadType;

public class PayloadFactory {

    public Payload create(Object content, PayloadType type) {
        return new Payload(content, type);
    }

    public Payload create() {
        return new Payload("content", PayloadType.STRING);
    }

    public static PayloadFactory instance() {
        return new PayloadFactory();
    }
}
