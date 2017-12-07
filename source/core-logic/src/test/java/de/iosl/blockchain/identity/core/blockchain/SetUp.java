package de.iosl.blockchain.identity.core.blockchain;

import de.iosl.blockchain.identity.eba.BlockchainAccess;
import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static de.iosl.blockchain.identity.eba.main.AccountAccess.pathToFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@Slf4j
public class SetUp extends AbstractEthereumTest{


    @Test
    public void testCreateAccountFromScratch() throws Exception {
        String password = "password";

        BlockchainAccess blockchainAccess = new BlockchainAccess();
        blockchainAccess.setAccountAccess(new AccountAccess());
        Account account = blockchainAccess.createWallet(password);

        log.debug(account.getAddress());
        log.debug(String.valueOf(account.getPrivateKey()));
        log.debug(String.valueOf(account.getPublicKey()));




//        String password = "password";
//
//        EthAccounts accountsResponse = web3j.ethAccounts().sendAsync().get();
//        accountsResponse.getAccounts().stream().forEach(account -> {System.out.println(account);});
//        System.out.println(accountsResponse.getAccounts().size());
//
//
//
//
//        // create new private/public key pair
//        ECKeyPair keyPair = Keys.createEcKeyPair();
//
//        BigInteger publicKey = keyPair.getPublicKey();
//        String publicKeyHex = Numeric.toHexStringWithPrefix(publicKey);
//
//        BigInteger privateKey = keyPair.getPrivateKey();
//        String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);
//
//        // create credentials + address from private/public key pair
//        Credentials credentials = Credentials.create(new ECKeyPair(privateKey, publicKey));
//        String address = credentials.getAddress();
//
//        // print resulting data of new account
//        System.out.println("private key: '" + privateKeyHex + "'");
//        System.out.println("public key: '" + publicKeyHex + "'");
//        System.out.println("address: '" + address + "'\n");
//
//
//        // connect to node
//
//// send asynchronous requests to get balance
//        EthGetBalance ethGetBalance = web3j
//                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
//                .sendAsync()
//                .get();
//
//        BigInteger wei = ethGetBalance.getBalance();
//        System.out.println("wei:" +wei +"  -  "+ ethGetBalance.getJsonrpc());
//
//
//
//        String walletFileName = WalletUtils.generateFullNewWalletFile(password,new File(WALLET_DIR+"/wallet"));
////        String[] fetchAddress=walletFileName.split("--");
////
////        String adress = fetchAddress[fetchAddress.length-1].split("\\.")[0];
//
//        Credentials credential = WalletUtils.loadCredentials(
//                "your password",
//                "/path/to/walletfile");


//
//
//        Web3j web3 = Web3j.build(new HttpService());
//
//
//        String walletFileName = WalletUtils.generateFullNewWalletFile(password,
//                new File("./cred"));
//
//
//        String[] fetchAddress=walletFileName.split("--");
//        String getAddress = fetchAddress[fetchAddress.length-1].split("\\.")[0];


//        Account userWallet = new Account();
//        userWallet.setAddress("0x" + getAddress);
//
//        userWallet.setUser(userRepository.findById(userId));
//        userWallet.setWalletType(WalletType.TOKEN);
//        userWallet.setWalletFileName(walletFileName);
//
//        userWallet.setPassword(password);
//
//
//        UserWallet savedUserWallet = userWalletRepository.save(userWallet);
//
//        result.put("userId", savedUserWallet.getUser().getId());
//        result.put("responseMessage", "success");
//        result.put("message", "User Wallet created successfully");
//        result.put("phassprase", password);
//        result.put("address", savedUserWallet.getAddress());
//        result.put("walletFileName", savedUserWallet.getWalletFileName());
//        return result;



//        // test (1) check if it's possible to transfer funds to new address
//        BigInteger amountWei = Convert.toWei("0.131313", Convert.Unit.ETHER).toBigInteger();
//        transferWei(getCoinbase(), address, amountWei);
//
//        BigInteger balanceWei = getBalanceWei(address);
//        BigInteger nonce = getNonce(address);
//
//        assertEquals("Unexpected nonce for 'to' address", BigInteger.ZERO, nonce);
//        assertEquals("Unexpected balance for 'to' address", amountWei, balanceWei);
//
//        // test (2) funds can be transferred out of the newly created account
//        BigInteger txFees = Web3jConstants.GAS_LIMIT_ETHER_TX.multiply(Web3jConstants.GAS_PRICE);
//        RawTransaction txRaw = RawTransaction
//                .createEtherTransaction(
//                        nonce,
//                        Web3jConstants.GAS_PRICE,
//                        Web3jConstants.GAS_LIMIT_ETHER_TX,
//                        getCoinbase(),
//                        amountWei.subtract(txFees));
//
//        // sign raw transaction using the sender's credentials
//        byte[] txSignedBytes = TransactionEncoder.signMessage(txRaw, credentials);
//        String txSigned = Numeric.toHexString(txSignedBytes);
//
//        // send the signed transaction to the ethereum client
//        EthSendTransaction ethSendTx = web3j
//                .ethSendRawTransaction(txSigned)
//                .sendAsync()
//                .get();
//
//        Response.Error error = ethSendTx.getError();
//        String txHash = ethSendTx.getTransactionHash();
//        assertNull(error);
//        assertFalse(txHash.isEmpty());
//
//        waitForReceipt(txHash);
//
//        assertEquals("Unexpected nonce for 'to' address", BigInteger.ONE, getNonce(address));
//        assertTrue("Balance for 'from' address too large: " + getBalanceWei(address), getBalanceWei(address).compareTo(txFees) < 0);
//        System.out.println("accountsize:"+accountsResponse.getAccounts().size());
    }

}
