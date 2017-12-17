package de.iosl.blockchain.identity.core.factories;

import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;

import java.util.UUID;

/**
 * Created by Marvin Petzolt on 17.12.17.
 */
public class ClaimFactory {

    private PayloadFactory payloadFactory = PayloadFactory.instance();
    private ProviderFactory providerFactory = ProviderFactory.instance();

    public Claim create() {
        return create(UUID.randomUUID().toString());
    }

    public Claim create(String claimId) {
        return create(claimId, providerFactory.create(), payloadFactory.create());
    }

    private Claim create(String claimId, Provider provider, Payload payload) {
        return new Claim(claimId, null, null, provider, payload);
    }

    public static ClaimFactory instance() {
        return new ClaimFactory();
    }
}
