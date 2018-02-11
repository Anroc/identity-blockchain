package de.iosl.blockchain.identity.core.shared.eba.main.util;

import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Clouser_sol_ClouserContract;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.EBAException;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.NoClouserCouldBeAddedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ClouserContractUtils {

    public String deployClouserContract(Account sender, String recipient, PermissionContractContent permissionContractContent, Web3j web3j) {
        try {
            log.info("wallet balance before deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));
            Clouser_sol_ClouserContract contract = Clouser_sol_ClouserContract.deploy(
                    web3j,
                    sender.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX,
                    recipient,
                    permissionContractContent.getRequesterAddress(),
                    BigInteger.valueOf((long) permissionContractContent.getClosureContent().getEncryptedRequests().size()),
                    permissionContractContent.getClosureContent().getEncryptedKey()
            ).send();
            if (contract == null)
                throw new NullPointerException("Permission Contract is null. Clouser Contract could not be created");

            log.info("ClouserContract Address: {}", contract.getContractAddress());
            log.info("wallet balance after deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));

            log.info("set requested clousers");
            for (String clouserRequest:permissionContractContent.getClosureContent().getEncryptedRequests()) {
                try {
                    contract.addClouserAsRequestingProvider(clouserRequest).send();
                } catch (Exception e) {
                    contract.kill().send();
                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the ClouserContract", contract);
                }
            }

            log.info("All clousers requested are in the contract");
            return contract.getContractAddress();
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    public ClosureContent getClouserContractContent(Account account, String smartContractAddress, Web3j web3j){
        try {
            log.info("get clouser contract");
            Clouser_sol_ClouserContract contract = Clouser_sol_ClouserContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );

            if (contract == null)
                throw new NullPointerException("Clouser Contract is null. Clouser Contract does not exists");
            log.info("ClouserContract Address: {}", contract.getContractAddress());


            String owner =contract.getOwner().send();
            String user = contract.getUser().send();
            if(account.equals(user)){
                log.info("");
                return getClouserContractContentAsUser(contract, web3j);
            }else{
                return getClouserContractContentProvider(contract, web3j);
            }
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    private ClosureContent getClouserContractContentProvider(Clouser_sol_ClouserContract contract, Web3j web3j) {
        try {
            String temp = contract.getApprovedClouserAmount().send()+"";
            int amount = Integer.parseInt(temp);
            Set<String> encryptedRequests = new HashSet<>();
            String encryptedKey = "";

            log.info("Amount of clouser in contract is: {} . Now get all clousers form blockchain", amount);
            for (int i = 0; i < amount; i++) {
                //TransactionReceipt transactionReceipt = contract.getRequestedClouserAsUserAndDeleteIndex(BigInteger.valueOf(amount)).send();
                String approvedClouser = contract.getApprovedClouserByIndex(BigInteger.valueOf(amount)).send();
                encryptedRequests.add(approvedClouser);
            }

            log.info("Got all clousers");
            log.info("get encrypted key");
            encryptedKey = contract.getEncryptedKey().send();

            log.info("Got all encyptedKey");
            log.info("get requestingProvider");

            String requestingProvider = contract.getRequestingProvider().send();

            ClosureContent closureContent = new ClosureContent(encryptedRequests, encryptedKey);
            return closureContent;

        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    private ClosureContent getClouserContractContentAsUser(Clouser_sol_ClouserContract contract, Web3j web3j) {

        try {

            String temp = contract.getRequestedClouserAmount().send()+"";
            int amount = Integer.parseInt(temp);
            Set<String> encryptedRequests = new HashSet<>();
            String encryptedKey = "";

            log.info("Amount of clouser in contract is: {} . Now get all clousers form blockchain", amount);
            for (int i = 0; i < amount; i++) {
                //TransactionReceipt transactionReceipt = contract.getRequestedClouserAsUserAndDeleteIndex(BigInteger.valueOf(amount)).send();
                String requestedClouser = contract.getRequestedClouserByIndex(BigInteger.valueOf(amount)).send();
                encryptedRequests.add(requestedClouser);
            }

            log.info("Got all clousers");
            log.info("get encrypted key");
            encryptedKey = contract.getEncryptedKey().send();

            log.info("Got all encyptedKey");
            log.info("get requestingProvider");

            String requestingProvider = contract.getRequestingProvider().send();

            ClosureContent closureContent = new ClosureContent(encryptedRequests, encryptedKey);
            return closureContent;

        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    public void setApprovedClaims(Account account, String smartContractAddress, ClosureContent clouserContent, Web3j web3j) {

        try {
            log.info("get clouser for user");
            Clouser_sol_ClouserContract contract = Clouser_sol_ClouserContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract does not exists");
            log.info("ClouserContract Address: {}", contract.getContractAddress());

            log.info("set approved clousers");
            for (String clouser:clouserContent.getEncryptedRequests()) {
                try {
                    contract.addToApprovedClouserAsUser(clouser).send();
                } catch (Exception e) {
                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the ClouserContract", contract);
                }
            }

            log.info("approved clousers are in contract");
            log.info("set amount variable of approved clousers");

            contract.setApprovedClouserAmount(BigInteger.valueOf(clouserContent.getEncryptedRequests().size()));

            log.info("approved clousers are in contract");


        } catch (Exception e) {
            throw new EBAException(e);
        }

    }
}
