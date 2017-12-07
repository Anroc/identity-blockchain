package de.iosl.blockchain.identity.eba.main;


import de.iosl.blockchain.identity.eba.main.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static de.iosl.blockchain.identity.core.register.keychain.KeyChainService.WALLET_DIR;
@Slf4j
@Component
public class AccountAccess {

    public static String pathToFile = WALLET_DIR+"wallet"+File.separator;

    public Account createAccount(String password){

        try {
            File directory = new File(pathToFile);
            if (!directory.exists()){
                log.debug("directory does not exists, create dir");
                Files.createDirectories(Paths.get(pathToFile));
            }

            String walletName = WalletUtils.generateFullNewWalletFile(password,new File(pathToFile));
            return accessWallet(password, walletName);
        } catch (NoSuchAlgorithmException | NoSuchProviderException |InvalidAlgorithmParameterException| CipherException blockchainexception) {
            throw new RuntimeException(blockchainexception.getMessage(),blockchainexception.getCause());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(),ioe.getCause());
        }
    }

    public Account accessWallet(String password, String walletName){
        try {
            Credentials credentials = WalletUtils.loadCredentials(
                    password,
                    pathToFile+walletName);
            Account account = new Account(
                    credentials.getAddress(),
                    credentials.getEcKeyPair().getPublicKey(),
                    credentials.getEcKeyPair().getPrivateKey(),
                    walletName
            );
            return account;
        } catch (CipherException blockchainexception) {
            throw new RuntimeException(blockchainexception.getMessage(),blockchainexception.getCause());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(),ioe.getCause());
        }
    }
}
