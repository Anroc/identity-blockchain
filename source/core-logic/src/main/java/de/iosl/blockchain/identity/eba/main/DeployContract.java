package de.iosl.blockchain.identity.eba.main;

import de.iosl.blockchain.identity.eba.contracts.*;
import de.iosl.blockchain.identity.eba.main.Account;
import de.iosl.blockchain.identity.eba.main.AccountAccess;
import de.iosl.blockchain.identity.eba.main.util.Web3jConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;

import java.io.File;
import java.io.IOException;

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
            contract.
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
