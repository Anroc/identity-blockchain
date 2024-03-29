package de.iosl.blockchain.identity.core.shared.keychain.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedKeyChain {

    private EncryptedKeySpec privateKey;
    private EncryptedKeySpec publicKey;

    private String algorithm;
    private int bitSecurity;

    private String account;

    private String registerSmartContractAddress;
}
