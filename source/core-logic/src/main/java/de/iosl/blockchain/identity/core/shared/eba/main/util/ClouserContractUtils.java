package de.iosl.blockchain.identity.core.shared.eba.main.util;

import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Closure_sol_ClosureContract;
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
            log.info("Amount of clousers to set in contract: {} with assigend user: {} and requesting provider: {}", permissionContractContent.getClosureContent().getEncryptedRequests().size(), recipient, permissionContractContent.getRequesterAddress());

            Closure_sol_ClosureContract contract = Closure_sol_ClosureContract.deploy(
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
                    contract.addClosureAsRequestingProvider(clouserRequest).send();
                } catch (Exception e) {
                    contract.kill().send();
                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the ClosureContract", contract);
                }
            }

            log.info("All clousers requested are in the contract with amount: {} ", contract.getRequestedClosureAmount().send());
            return contract.getContractAddress();
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    public ClosureContent getClouserContractContent(Account account, String smartContractAddress, Web3j web3j){
        try {
            log.info("get clouser contract");
            Closure_sol_ClosureContract contract = Closure_sol_ClosureContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );

            if (contract == null)
                throw new NullPointerException("Clouser Contract is null. Clouser Contract does not exists");
            log.info("ClouserContract exists with Address: {}", contract.getContractAddress());


            String owner =contract.getOwner().send();
            log.info("owner is: {}", owner);
            String user = contract.getUser().send();
            log.info("user is: {}", user);
            String provider = contract.getRequestingProvider().send();
            log.info("provider is: {}", provider);
            String requestingAccount = account.getAddress();
            log.info("account is: {}", requestingAccount);

            if(account.getAddress().equals(user)){
                log.info("user wants requested clousers");
                return getClouserContractContentAsUser(contract);
            }else{
                log.info("provider wants approved clousers");
                return getClouserContractContentProvider(contract);
            }
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    private ClosureContent getClouserContractContentProvider(Closure_sol_ClosureContract contract) {
        try {
            String temp = contract.getApprovedClosureAmount().send()+"";
            int amount = Integer.parseInt(temp);
            Set<String> encryptedRequests = new HashSet<>();
            String encryptedKey = "";

            log.info("Amount of ApprovedClouser as provider in contract is: {} . Now get all clousers form blockchain", amount);
            for (int i = 0; i < amount; i++) {
                //TransactionReceipt transactionReceipt = contract.getRequestedClouserAsUserAndDeleteIndex(BigInteger.valueOf(amount)).send();
                String approvedClouser = contract.getApprovedClosureByIndex(BigInteger.valueOf(i)).send();
                log.info("got one");
                encryptedRequests.add(approvedClouser);
            }

            log.info("Got all clousers");
            log.info("get encrypted key");
            encryptedKey = contract.getEncryptedKeyForApprovedClosure().send();
            log.info("Got all encyptedKey: {}",encryptedKey);

            ClosureContent closureContent = new ClosureContent(encryptedRequests, encryptedKey);
            return closureContent;

        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    private ClosureContent getClouserContractContentAsUser(Closure_sol_ClosureContract contract) {

        try {

            String temp = contract.getRequestedClosureAmount().send()+"";
            int amount = Integer.parseInt(temp);
            Set<String> encryptedRequests = new HashSet<>();
            String encryptedKey = "";

            log.info("Amount of requested clousers in contract is: {} . Now get all clousers form blockchain", amount);
            for (int i = 0; i < amount; i++) {
                //TransactionReceipt transactionReceipt = contract.getRequestedClouserAsUserAndDeleteIndex(BigInteger.valueOf(amount)).send();
                log.info("try to get index {} of the requestedClouserSet in Contract", i);
                String requestedClouser = contract.getRequestedClosureByIndex(BigInteger.valueOf(i)).send();
                encryptedRequests.add(requestedClouser);
            }

            log.info("Got all clousers");
            log.info("get encrypted key");
            encryptedKey = contract.getEncryptedKeyForRequestedClosure().send();
            log.info("Got all encyptedKey: {}",encryptedKey);

            ClosureContent closureContent = new ClosureContent(encryptedRequests, encryptedKey);
            return closureContent;

        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    public void setApprovedClosure(Account account, String smartContractAddress, ClosureContent clouserContent, Web3j web3j) {

        try {
            log.info("get clouser for user because he has to approve");
            Closure_sol_ClosureContract contract = Closure_sol_ClosureContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract does not exists");
            log.info("ClouserContract Address: {}", contract.getContractAddress());

            log.info("set approved clousers with amount: {}", clouserContent.getEncryptedRequests().size());
            for (String clouser:clouserContent.getEncryptedRequests()) {
                try {
                    contract.addToApprovedClosureAsUser(clouser).send();
                    log.info("one clouser sended");
                } catch (Exception e) {
                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the ClouserContract", contract);
                }
            }

            log.info("set encrypted key for approved clouser: {}", clouserContent.getEncryptedKey());
            contract.setEncryptedKeyForApprovedClosure(clouserContent.getEncryptedKey());
            log.info("encrypted key for approved clouser is now in contract with key: {}",contract.getEncryptedKeyForApprovedClosure().send());

            log.info("amount variable of approved clousers will be set with amount: {}", BigInteger.valueOf(clouserContent.getEncryptedRequests().size()));
            contract.setApprovedClosureAmount(BigInteger.valueOf(clouserContent.getEncryptedRequests().size())).send();
            log.info("approved clousers and their amount are in contract with amount {}", contract.getApprovedClosureAmount().send());




        } catch (Exception e) {
            throw new EBAException(e);
        }

    }
}
