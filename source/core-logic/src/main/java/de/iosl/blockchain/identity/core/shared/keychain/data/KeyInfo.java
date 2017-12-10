package de.iosl.blockchain.identity.core.shared.keychain.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.KeyPair;

@Data
@AllArgsConstructor
public class KeyInfo {

    private KeyPair keyPair;
    private String accountPath;

}
