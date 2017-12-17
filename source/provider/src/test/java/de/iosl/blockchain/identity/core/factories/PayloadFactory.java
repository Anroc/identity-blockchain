package de.iosl.blockchain.identity.core.factories;

import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;

public class PayloadFactory {

    public Payload create(Object content, Payload.Type type) {
        return new Payload(content, type);
    }

    public Payload create() {
        return new Payload("content", Payload.Type.STRING);
    }

    public static PayloadFactory instance() {
        return new PayloadFactory();
    }
}
