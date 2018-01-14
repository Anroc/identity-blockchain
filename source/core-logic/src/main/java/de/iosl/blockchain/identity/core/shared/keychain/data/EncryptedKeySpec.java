package de.iosl.blockchain.identity.core.shared.keychain.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedKeySpec {
    private String key;
    private String iv;
}