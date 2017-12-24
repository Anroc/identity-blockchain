package de.iosl.blockchain.identity.core.factories;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.shared.claims.payload.Payload;
import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;

import java.util.UUID;

/**
 * Created by Marvin Petzolt on 17.12.17.
 */
public class ClaimFactory {

    private PayloadFactory payloadFactory = PayloadFactory.instance();
    private ProviderFactory providerFactory = ProviderFactory.instance();

    public ProviderClaim create() {
        return create(UUID.randomUUID().toString());
    }

    public ProviderClaim create(String claimId) {
        return create(claimId, providerFactory.create(), payloadFactory.create());
    }

    private ProviderClaim create(String claimId, Provider provider, Payload payload) {
        return new ProviderClaim(claimId, null, null, provider, payload);
    }

    public static ClaimFactory instance() {
        return new ClaimFactory();
    }
}
