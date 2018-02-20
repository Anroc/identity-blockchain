package de.iosl.blockchain.identity.lib.dto.beats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrityCheck {

    private String encryptedSessionKey;
    private String encryptedSalt;

    private Map<String, String> permissionIntegrityHashes;
    private Map<String, String> closureIntegrityHashes;
}
