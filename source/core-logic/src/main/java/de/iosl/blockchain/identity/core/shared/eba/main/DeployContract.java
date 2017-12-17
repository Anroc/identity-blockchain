package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.main.contracts.*;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Slf4j
@Component
public class DeployContract {

    public void deployRegistrarContract(String password, Account account, Web3j web3j) {
        try {
            Registrar_sol_FirstContract contract = Registrar_sol_FirstContract.deploy(
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_REGISTRAR_TX
            ).send();
            log.info("Smart Contract Address: {}, Variable: {}", contract.getContractAddress(), contract.getVariable());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
