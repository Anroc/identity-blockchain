package de.iosl.blockchain.identity.core.user.register;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClient;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.shared.keychain.data.KeyInfo;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;

@Service
public class RegisterService {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private KeyChainService keyChainService;
    @Autowired
    private EBAInterface ebaInterface;


    public String register(String password) throws IOException {
        keyChainService.createDir(keyChainService.getDefaultWalletDir());

        Account account = ebaInterface.createWallet(password, Paths.get(KeyChain.WALLET_DIR));
        keyChain.setAccount(account);

        KeyPair keyPair = createKeyPair(password, account.getFile().getAbsolutePath());
        keyChain.setRsaKeyPair(keyPair);

        registerToDS();
        keyChain.setRegistered(true);

        return keyChain.getAccount().getAddress();
    }

    public String login(String password) throws IOException {
        KeyInfo keyInfo = readKeyPair(password);
        keyChain.setRsaKeyPair(keyInfo.getKeyPair());

        Account account = ebaInterface.accessWallet(password, Paths.get(keyInfo.getAccountPath()).toFile());
        keyChain.setAccount(account);

        registerToDS();
        keyChain.setRegistered(true);

        return keyChain.getAccount().getAddress();
    }

    public void logout() {
        keyChain.setRegistered(false);
        keyChain.setAccount(null);
        keyChain.setRsaKeyPair(null);
    }

    private KeyInfo readKeyPair(String password) throws IOException {
        return keyChainService.readKeyChange(keyChainService.getDefaultWalletFile(), password);
    }

    private KeyPair createKeyPair(String password, String absolutePath) throws IOException {
        KeyPair keyPair = CryptEngine.generate()
                .with(1024)
                .string()
                .rsa()
                .getAsymmetricCipherKeyPair();

        keyChainService.saveKeyChain(keyPair, keyChainService.getDefaultWalletFile(), password, absolutePath);
        return keyPair;
    }

    private void registerToDS() {
        discoveryClient.register(
                keyChain.getAccount().getAddress(),
                KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64(),
                keyChain.getAccount().getPrivateKey());
    }
}
