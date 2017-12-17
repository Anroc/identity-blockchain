package de.iosl.blockchain.identity.core.shared.eba.main;


import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;
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
            BigInteger amountWei = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();
            String transactionHash =transferWeiFromCoinbaseToCreatedAccount(account.getAddress(),amountWei,web3j);
            log.info("new wallet amount: {}", getBalanceWei(web3j, account.getAddress()));
            return account;

        } catch ( ExecutionException| InterruptedException | NoSuchAlgorithmException | NoSuchProviderException |InvalidAlgorithmParameterException| CipherException |IOException exception) {
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
                    credentials.getEcKeyPair().getPublicKey(),
                    file,
                    credentials
            );
            return account;
        } catch (IOException| CipherException exception) {
            throw new RuntimeException(exception.getMessage(),exception.getCause());
        }
    }

    private String transferWeiFromCoinbaseToCreatedAccount(String to, BigInteger amountWei, Web3j web3j) throws ExecutionException, InterruptedException {

        EthCoinbase coinbase = web3j.ethCoinbase().sendAsync().get();
        
        BigInteger nonce = getNonce(web3j,coinbase.getAddress());
        Transaction transaction = Transaction.createEtherTransaction(
                coinbase.getAddress(), nonce, Web3jConstants.GAS_PRICE, Web3jConstants.GAS_LIMIT_ETHER_TX, to, amountWei);

        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
        log.info("transferEther. nonce: " + nonce + " amount: " + amountWei + " to: " + to);

        String txHash = ethSendTransaction.getTransactionHash();
        waitForReceipt(web3j,txHash);

        return txHash;

    }

    BigInteger getNonce(Web3j web3j,String address) throws ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
    }

    TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) throws ExecutionException, InterruptedException {
        int attempts = Web3jConstants.CONFIRMATION_ATTEMPTS;
        int sleep_millis = Web3jConstants.SLEEP_DURATION;

        Optional<TransactionReceipt> receipt = getReceipt(web3j, transactionHash);

        while(attempts-- > 0 && !receipt.isPresent()) {
            Thread.sleep(sleep_millis);
            receipt = getReceipt(web3j, transactionHash);
        }

        if (attempts <= 0) {
            throw new RuntimeException("No Tx receipt received");
        }

        return receipt.get();
    }

    public static Optional<TransactionReceipt> getReceipt(Web3j web3j, String transactionHash) throws ExecutionException, InterruptedException {
        EthGetTransactionReceipt receipt = web3j
                .ethGetTransactionReceipt(transactionHash)
                .sendAsync()
                .get();

        return receipt.getTransactionReceipt();
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
