package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.contracts.Registrar_sol_FirstContract;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class RegistrarContractUtils {

    public Contract deployRegistrarContract(Account account, Web3j web3j) {
        try {
            log.info("wallet balance before deployment", getBalanceWei(web3j, account.getAddress()));
            Registrar_sol_FirstContract contract = Registrar_sol_FirstContract.deploy(
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_REGISTRAR_TX
            ).send();
            if(contract==null)
                return null; //create exception
            log.info("Smart Contract Address: {}, Approval: {}", contract.getContractAddress(), contract.getApproval().send());
            log.info("wallet balance after deployment", getBalanceWei(web3j, account.getAddress()));
            return contract;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private BigInteger getBalanceWei(Web3j web3j, String address) throws ExecutionException, InterruptedException {
        EthGetBalance balance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        return balance.getBalance();
    }

    public TransactionReceipt approveRegistrarContractAsGovernment(Account governmentAccount, String contractAddress, Boolean decision, Web3j web3j){
        try {
            log.info("Set decision in contract: {}", decision);
            Registrar_sol_FirstContract contract = Registrar_sol_FirstContract.load(
                    contractAddress,
                    web3j,
                    governmentAccount.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_REGISTRAR_TX);

            if(contract==null)
                return null; //create exception

            TransactionReceipt transactionReceipt = contract.setApproval(decision).send();
            log.info("Smart Contract Address: {}, Approval: {}", contract.getContractAddress(), contract.getApproval().send());
            log.info("transaction receipt: {}" ,transactionReceipt);
            return transactionReceipt;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
