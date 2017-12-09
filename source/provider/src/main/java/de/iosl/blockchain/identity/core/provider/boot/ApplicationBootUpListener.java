package de.iosl.blockchain.identity.core.provider.boot;

import de.iosl.blockchain.identity.core.provider.security.CredentialConfig;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.config.ClientType;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.shared.registry.DiscoveryClient;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;

@Slf4j
@Component
public class ApplicationBootUpListener {
    private KeyChain keyChain;

    @Autowired private CredentialConfig credentialConfig;
    @Autowired private DiscoveryClient discoveryClient;
    @Autowired private EBAInterface ebaInterface;
    @Autowired private BlockchainIdentityConfig blockchainIdentityConfig;

    @Bean
    public KeyChain initKeyChain() {
        return this.keyChain;
    }

    @PostConstruct
    public void register() {
        Account account = null;
        if(blockchainIdentityConfig.getType() == ClientType.GOVERMENT) {
            File file = Paths.get(KeyChain.WALLET_DIR + KeyChain.GOV_WALLET).toFile();

            account = ebaInterface.accessWallet(credentialConfig.getPassword(), file);
        }
        KeyChain keyChain = new KeyChain();
        keyChain.setAccount(account);
        keyChain.setRsaKeyPair(CryptEngine.generate().string().rsa().generateKeyPair());
        this.keyChain = keyChain;

        String publicKey = KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64();

        discoveryClient.register(account.getAddress(), publicKey, account.getPrivateKey());
        log.info("Registered ethID {} to DiscoveryService", account.getAddress());
    }
}
