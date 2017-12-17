package de.iosl.blockchain.identity.core.factories;

import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;

import java.util.UUID;

public class ProviderFactory {

    public Provider create() {
        return create("eth_id" + UUID.randomUUID().toString(), "some_name");
    }

    public Provider create(String ethId, String name) {
        return new Provider(ethId, name);
    }

    public static ProviderFactory instance() {
        return new ProviderFactory();
    }
}
