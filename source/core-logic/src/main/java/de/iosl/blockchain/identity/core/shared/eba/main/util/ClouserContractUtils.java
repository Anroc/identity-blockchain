package de.iosl.blockchain.identity.core.shared.eba.main.util;

import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Clouser_sol_ClouserContract;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Permission_sol_PermissionContract;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.EBAException;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.NoClouserCouldBeAddedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

@Slf4j
@Component
public class ClouserContractUtils {

    public String deployClouserContract(Account sender, String recipient, PermissionContractContent permissionContractContent, Web3j web3j) {
        String contractAddress = "";
        try {
            log.info("wallet balance before deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));
            Clouser_sol_ClouserContract contract = Clouser_sol_ClouserContract.deploy(
                    web3j,
                    sender.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX,
                    recipient,
                    BigInteger.valueOf((long) permissionContractContent.getClosureContent().getEncryptedRequests().size()),
                    permissionContractContent.getClosureContent().getEncryptedKey()
            ).send();
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract could not be created");

            log.info("PermissionContract Address: {}, approvedClaims: {}", contract.getContractAddress());
            log.info("wallet balance after deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));

            for (String clouserRequest:permissionContractContent.getClosureContent().getEncryptedRequests()) {
                try {
                    contract.addClouserAsRequestingProvider(clouserRequest).send();
                } catch (Exception e) {
                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the CLouserContract", contract);
                }
            }
//            permissionContractContent.getClosureContent().getEncryptedRequests().stream().forEach(encryptedRequests -> {
//                try {
//                    contract.addClouserAsRequestingProvider(encryptedRequests).send();
//                } catch (Exception e) {
//                    throw new NoClouserCouldBeAddedException("One clouserRequest could not be added into the CLouserContract", contract);
//                }
//            });

            return contract.getContractAddress();

        } catch (NullPointerException ne){
            throw new EBAException(ne);
        } catch (NoClouserCouldBeAddedException clouserAddingExc) {
            try {
                clouserAddingExc.getContract().kill().send();
            } catch (Exception e) {
                throw new EBAException(e);
            }
            throw new EBAException(clouserAddingExc);
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }

    public PermissionContractContent getPermissionContractContent(Account account, String smartContractAddress, Web3j web3j) {
        return null;
    }

    public void setApprovedClaims(Account account, String smartContractAddress, PermissionContractContent permissionContractContent, Web3j web3j) {
    }
}
