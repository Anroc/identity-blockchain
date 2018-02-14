package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class AccountAccess {


    public Account createAccount(String password, Path path, Web3j web3j){

        try {
            if (!path.toFile().exists()){
                log.debug("directory does not exists, create dir");
                Files.createDirectories(path);
            }
            String walletName = WalletUtils.generateFullNewWalletFile(
                    password,
                    path.toFile()
            );

            Account account =accessWallet(
                    password,
                    new File(path.toFile().getAbsolutePath()+File.separator+walletName)
            );
            log.info("wallet amount after creation: {}", getBalanceWei(web3j, account.getAddress()));
            TransactionReceipt transactionReceipt= Web3jUtils.transferWeiFromCoinbaseToCreatedAccount(account,Web3jConstants.amountToEther(Web3jConstants.DEFAULT_START_AMOUNT),web3j);
            log.info("new wallet amount: {}", getBalanceWei(web3j, account.getAddress()));
            return account;

        } catch ( TransactionException|ExecutionException| InterruptedException | NoSuchAlgorithmException | NoSuchProviderException |InvalidAlgorithmParameterException| CipherException |IOException exception) {
            throw new RuntimeException(exception);
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
                    file,
                    credentials
            );
            return account;
        } catch (IOException| CipherException exception) {
            throw new RuntimeException(exception);
        }
    }

    BigInteger getBalanceWei(Web3j web3j, String address) throws ExecutionException, InterruptedException {
        EthGetBalance balance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return balance.getBalance();
    }

//    BigInteger getNonce(Web3j web3j,String address) throws ExecutionException, InterruptedException {
//        return Web3jUtils.getNonce(web3j, address);
//    }
//
//    TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) throws ExecutionException, InterruptedException {
//        return Web3jUtils.waitForReceipt(web3j, transactionHash);
//    }
//
//    BigInteger getBalanceWei(Web3j web3j, String address) throws ExecutionException, InterruptedException {
//        return Web3jUtils.getBalanceWei(web3j, address);
//    }

}
