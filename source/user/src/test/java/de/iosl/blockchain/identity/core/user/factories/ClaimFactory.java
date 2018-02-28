package de.iosl.blockchain.identity.core.user.factories;

import de.iosl.blockchain.identity.core.shared.api.data.dto.*;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.lib.dto.ECSignature;

import java.util.Date;
import java.util.UUID;

public class ClaimFactory {

    private PayloadFactory payloadFactory = PayloadFactory.instance();
    private ProviderFactory providerFactory = ProviderFactory.instance();

    public UserClaim create() {
        return create(UUID.randomUUID().toString());
    }

    public UserClaim create(String claimId) {
        return create(claimId, providerFactory.create(), payloadFactory.create(), "0xabc");
    }

    public UserClaim create(String claimId, ClaimType claimType, Object payload) {
        return create(claimId, providerFactory.create(), payloadFactory.create(payload, claimType), "0xabc");
    }

    public UserClaim create(String claimId, ClaimType claimType, Object payload, String targetUserId) {
        return create(claimId, providerFactory.create(), payloadFactory.create(payload, claimType), targetUserId);
    }

    private UserClaim create(String claimId, Provider provider, Payload payload, String targetUserId) {
        return new UserClaim(
                new ClaimDTO(
                        new SignedRequest<>(
                                new SignedClaimDTO(
                                        "0x123", claimId, new Date(), new ProviderDTO(provider), new PayloadDTO(payload)
                                ),
                                new ECSignature("r", "s", (byte) 3)
                        ),
                        null
                ),
                targetUserId);
    }

    public static ClaimFactory instance() {
        return new ClaimFactory();
    }
}
