package de.iosl.blockchain.identity.core.provider.boot;

import de.iosl.blockchain.identity.core.provider.config.CredentialConfig;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.config.ClientType;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClient;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Paths;

import static de.iosl.blockchain.identity.crypt.sign.EthereumSigner.addressFromPublicKey;

@Slf4j
@Component
public class ApplicationBootUpListener {

    @Autowired private KeyChain keyChain;
    @Autowired private CredentialConfig credentialConfig;
    @Autowired private DiscoveryClient discoveryClient;
    @Autowired private EBAInterface ebaInterface;
    @Autowired private BlockchainIdentityConfig blockchainIdentityConfig;

    @PostConstruct
    public void register() {
        Account account = null;
        if(blockchainIdentityConfig.getType() == ClientType.GOVERNMENT) {
            File file = Paths.get(KeyChain.WALLET_DIR + KeyChain.GOV_WALLET).toFile();

            account = ebaInterface.accessWallet(credentialConfig.getPassword(), file);

            keyChain.setAccount(account);
            keyChain.setRsaKeyPair(CryptEngine.generate().string().rsa().generateKeyPair());

            String publicKey = KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64();

            discoveryClient.register(account.getAddress(), publicKey, account.getPrivateKey());

            keyChain.setRegistered(true);
            log.info("Registered ethID {} to DiscoveryService", account.getAddress());

            log.info("Registered public key {} of address {}", account.getPublicKey(), addressFromPublicKey(account.getPublicKey()));
        }
    }
}
