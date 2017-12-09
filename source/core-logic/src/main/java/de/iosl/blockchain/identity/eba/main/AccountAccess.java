package de.iosl.blockchain.identity.eba.main;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Slf4j
@Component
public class AccountAccess {

    public Account createAccount(String password, Path path){

        try {
            if (!path.toFile().exists()){
                log.debug("directory does not exists, create dir");
                Files.createDirectories(path);
            }
            String walletName = WalletUtils.generateFullNewWalletFile(
                    password,
                    path.toFile()
            );
            return accessWallet(
                    password,
                    new File(path.toFile().getAbsolutePath()+File.separator+walletName)
            );
        } catch (NoSuchAlgorithmException | NoSuchProviderException |InvalidAlgorithmParameterException| CipherException |IOException exception) {
            throw new RuntimeException(exception.getMessage(),exception.getCause());
        }
    }

    public Account accessWallet(String password, File file){
        try {
            Credentials credentials = WalletUtils.loadCredentials(
                    password,
                    file);
            Account account = new Account(
                    credentials.getAddress(),
                    credentials.getEcKeyPair().getPublicKey(),
                    credentials.getEcKeyPair().getPrivateKey(),
                    file
            );
            return account;
        } catch (IOException| CipherException exception) {
            throw new RuntimeException(exception.getMessage(),exception.getCause());
        }
    }
}
