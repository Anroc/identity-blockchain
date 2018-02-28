package de.iosl.blockchain.identity.core.provider.factories;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.shared.api.data.dto.*;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import de.iosl.blockchain.identity.lib.dto.ECSignature;

import java.util.Date;
import java.util.UUID;

public class ClaimFactory {

    private PayloadFactory payloadFactory = PayloadFactory.instance();
    private ProviderFactory providerFactory = ProviderFactory.instance();

    public ProviderClaim create() {
        return create(UUID.randomUUID().toString());
    }

    public ProviderClaim create(String claimId) {
        return create(claimId, providerFactory.create(), payloadFactory.create());
    }

    public ProviderClaim create(String claimId, ClaimType claimType, Object payload) {
        return create(claimId, providerFactory.create(), payloadFactory.create(payload, claimType));
    }

    private ProviderClaim create(String claimId, Provider provider, Payload payload) {
        return new ProviderClaim(
                new ClaimDTO(
                        new SignedRequest<>(
                                new SignedClaimDTO(
                                        "0x123", claimId, new Date(), new ProviderDTO(provider), new PayloadDTO(payload)
                                ),
                                new ECSignature("r", "s", (byte) 3)
                        ),
                        null
                ));
    }

    public static ClaimFactory instance() {
        return new ClaimFactory();
    }
}
