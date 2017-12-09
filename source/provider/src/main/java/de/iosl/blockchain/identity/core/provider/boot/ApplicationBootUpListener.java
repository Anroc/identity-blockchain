package de.iosl.blockchain.identity.core.provider.boot;

import de.iosl.blockchain.identity.core.provider.security.CredentialConfig;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.keyChain.KeyChain;
import de.iosl.blockchain.identity.core.shared.registry.DiscoveryClient;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@Component
public class ApplicationBootUpListener {

    @Autowired
    private CredentialConfig credentialConfig;

    private KeyChain keyChain;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value(value = "classpath:/gov-wallet.json")
    private Resource accountDetails;

    @Bean
    public KeyChain initKeyChain() {
        return this.keyChain;
    }

    @PostConstruct
    public void register() {

        try {
            Credentials credential = WalletUtils.loadCredentials(
                    credentialConfig.getPassword(), accountDetails.getFile()
            );

            KeyChain keyChain = new KeyChain();
            keyChain.setCredentials(credential);
            keyChain.setRsaKeyPair(CryptEngine.generate().string().rsa().generateKeyPair());
            this.keyChain = keyChain;

            String publicKey = KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64();

            discoveryClient.register(credential.getAddress(), publicKey, credential.getEcKeyPair().getPrivateKey());
            log.info("Registered ethID {} to DiscoveryService", credential.getAddress());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
    }
}
