package de.iosl.blockchain.identity.core.shared.claims;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntegrityValidator {

    @Autowired
    private ClaimIntegrityFactory claimIntegrityFactory;

    public boolean validate(
            @NonNull String claimId,
            @NonNull String integrityHash,
            @NonNull Object claimValue,
            long salt) {



    }
}
