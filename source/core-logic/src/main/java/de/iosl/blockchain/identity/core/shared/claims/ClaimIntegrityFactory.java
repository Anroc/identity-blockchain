package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.claims.data.SharedClaim;
import de.iosl.blockchain.identity.crypt.asymmetic.StringAsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.ObjectSymmetricCryptEngine;
import de.iosl.blockchain.identity.lib.dto.beats.IntegrityCheck;
import lombok.NonNull;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClaimIntegrityFactory {

    private final SecureRandom secureRandom = new SecureRandom("l33t".getBytes());

    public IntegrityCheck buildIntegrityCheck(@NonNull PublicKey publicKey, List<SharedClaim> sharedClaims, List<Closure> closures) {
        StringAsymmetricCryptEngine stringAsymmetricCryptEngine = new StringAsymmetricCryptEngine();
        ObjectSymmetricCryptEngine objectSymmetricCryptEngine = new ObjectSymmetricCryptEngine();

        String salt = generateSalt();

        Map<String, String> pprIntegrity = new HashMap<>();
        sharedClaims.forEach(
                sharedClaim -> hashAndEncrypt(
                        pprIntegrity,
                        sharedClaim.getClaimValue().getPayload().getUnifiedValue(),
                        sharedClaim.getId(),
                        stringAsymmetricCryptEngine,
                        objectSymmetricCryptEngine,
                        salt
                )
        );

        Map<String, String> closureIntegrity = new HashMap<>();
        closures.forEach(
                closure -> hashAndEncrypt(
                        closureIntegrity,
                        true,
                        closure.getClaimID(),
                        stringAsymmetricCryptEngine,
                        objectSymmetricCryptEngine,
                        salt
                )
        );



    }

    private String generateSalt() {
        return Long.toHexString(secureRandom.nextLong());
    }

    private Map<String, String> hashAndEncrypt(
            Map<String, String> map,
            Object unifiedValue,
            String claimID,
            StringAsymmetricCryptEngine stringAsymmetricCryptEngine,
            ObjectSymmetricCryptEngine objectSymmetricCryptEngine,
            String salt) {

        String sha256 = stringAsymmetricCryptEngine.getSHA256Hash(
                objectSymmetricCryptEngine.serializeToBase64String(unifiedValue) + ":" + salt
        );
        map.put(claimID, sha256);
        return map;
    }

    private String keyToEncryptedBase64String(Key key) {

    }
}
