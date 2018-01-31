package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Permission_sol_PermissionContract;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.EBAException;
import de.iosl.blockchain.identity.core.shared.eba.main.util.ObjectToString;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Slf4j
@Component
public class PermissionContractUtils {

    public String deployPermissionContract(Account sender, String recipient, PermissionContractContent permissionContractContent, Web3j web3j){

        try {
            log.info("wallet balance before deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));
            // TODO @Timo: handle additional permissionContractContent.getClosureRequest() values

            Permission_sol_PermissionContract contract = Permission_sol_PermissionContract.deploy(
                    web3j,
                    sender.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX,
                    recipient,
                    ObjectToString.toString(permissionContractContent)
            ).send();
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract could not be created");

            log.info("PermissionContract Address: {}, approvedClaims: {}", contract.getContractAddress(), contract.getClaimsApproved());
            log.info("wallet balance after deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));
            return contract.getContractAddress();

        } catch (Exception e) {
            throw new EBAException(e);
        }
    }


    public PermissionContractContent getPermissionContractContent(Account account, String smartContractAddress, Web3j web3j) {
        try {
            log.info("get Claims from permission request");
            Permission_sol_PermissionContract contract = Permission_sol_PermissionContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract does not exists");

            PermissionContractContent permissionContractContent = (PermissionContractContent) ObjectToString.fromString(contract.getClaims().send());
            log.info("PermissionContract Address: {}, Amount NecessaryClaims: {}, Amount OptionalClaims: {} , RequesterAdress {}", contract.getContractAddress(), permissionContractContent.getRequiredClaims().size(), permissionContractContent.getOptionalClaims(), permissionContractContent.getRequesterAddress());
            return permissionContractContent;
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }


    public void setApprovedClaims(Account account, String smartContractAddress, PermissionContractContent permissionContractContent, Web3j web3j) {
        try {
            log.info("set Claims from permission request");
            Permission_sol_PermissionContract contract = Permission_sol_PermissionContract.load(
                    smartContractAddress,
                    web3j,
                    account.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX
            );
            if (contract == null)
                throw new NullPointerException("Contract is null. Contract does not exists");

            TransactionReceipt transactionReceipt = contract.setAndApproveClaims(ObjectToString.toString(permissionContractContent)).send();
            PermissionContractContent permissionContractContentFromContract = (PermissionContractContent) ObjectToString.fromString(contract.getClaims().send());
            log.info("PermissionContract Contract Address: {}, claims approved by user with transactionReceipt status: {} , with required claims: {} and optional claims {}", contract.getContractAddress(),transactionReceipt.getStatus(),permissionContractContentFromContract.getRequiredClaims().size(), permissionContractContentFromContract.getOptionalClaims());
        } catch (Exception e) {
            throw new EBAException(e);
        }
    }
}
