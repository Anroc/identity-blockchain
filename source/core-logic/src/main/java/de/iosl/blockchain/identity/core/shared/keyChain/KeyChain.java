package de.iosl.blockchain.identity.core.shared.keyChain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

import java.security.KeyPair;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class KeyChain {

    private Credentials credentials;
    private KeyPair rsaKeyPair;
}
