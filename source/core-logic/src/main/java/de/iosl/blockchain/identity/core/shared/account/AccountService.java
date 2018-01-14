package de.iosl.blockchain.identity.core.shared.account;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClient;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.shared.keychain.data.KeyInfo;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;

public abstract class AccountService {

    @Autowired
    public DiscoveryClient discoveryClient;
    @Autowired
    public KeyChain keyChain;
    @Autowired
    public KeyChainService keyChainService;
    @Autowired
    public EBAInterface ebaInterface;


    public String register(String password) throws IOException {
        return register(password, true);
    }

    public String register(String password, boolean deployContract) throws IOException {
        keyChainService.createDir(keyChainService.getDefaultWalletDir());

        Account account = ebaInterface.createWallet(password, Paths.get(KeyChain.WALLET_DIR));
        keyChain.setAccount(account);

        KeyPair keyPair = createKeyPair();
        keyChain.setRsaKeyPair(keyPair);

        if(deployContract) {
            String contractAddress = ebaInterface.deployRegistrarContract(account);;
            keyChain.setRegisterSmartContractAddress(contractAddress);
        }

        registerToDS();
        keyChain.setRegistered(true);

        keyChainService.saveKeyChain(keyChain, password, keyChainService.getDefaultWalletFile());

        return keyChain.getAccount().getAddress();
    }

    public String login(String password) throws IOException {
        KeyInfo keyInfo = readKeyPair(password);
        keyChain.setRsaKeyPair(keyInfo.getKeyPair());
        keyChain.setRegisterSmartContractAddress(keyInfo.getRegisterSmartContractAddress());

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

    private KeyPair createKeyPair() {
        KeyPair keyPair = CryptEngine.generate()
                .with(1024)
                .string()
                .rsa()
                .getAsymmetricCipherKeyPair();

        return keyPair;
    }

    private void registerToDS() {
        discoveryClient.register(
                keyChain.getAccount().getAddress(),
                KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64(),
                keyChain.getAccount().getPrivateKey());
    }
}
