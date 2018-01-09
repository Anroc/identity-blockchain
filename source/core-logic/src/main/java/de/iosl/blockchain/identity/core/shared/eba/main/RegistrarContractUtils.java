package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.main.contracts.*;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class DeployContract {

    public void deployRegistrarContract(String password, Account account, Web3j web3j) {
        try {
            log.info("wallet balance before deployment", getBalanceWei(web3j, account.getAddress()));
            Registrar_sol_FirstContract contract = Registrar_sol_FirstContract.deploy(
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_REGISTRAR_TX
            ).send();
            log.info("Smart Contract Address: {}, Variable: {}", contract.getContractAddress(), contract.getVariable());
            log.info("wallet balance after deployment", getBalanceWei(web3j, account.getAddress()));

            contract.getTransactionReceipt();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    BigInteger getBalanceWei(Web3j web3j, String address) throws ExecutionException, InterruptedException {
        EthGetBalance balance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return balance.getBalance();
    }

}
