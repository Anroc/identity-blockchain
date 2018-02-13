package de.iosl.blockchain.identity.core.shared.eba.main;

import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.contracts.Permission_sol_PermissionContract;
import de.iosl.blockchain.identity.core.shared.eba.main.exception.EBAException;
import de.iosl.blockchain.identity.core.shared.eba.main.util.ClouserContractUtils;
import de.iosl.blockchain.identity.core.shared.eba.main.util.ObjectToString;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Slf4j
@Component
public class PermissionContractUtils {

    @Autowired
    private ClouserContractUtils clouserContractUtils;

    public String deployPermissionContract(Account sender, String recipient, PermissionContractContent permissionContractContent, Web3j web3j){

        String clouserContractAddress=Web3jConstants.NO_CLOSURE_ADDRESS;
        if(permissionContractContent.getClosureContent()!=null){
            clouserContractAddress=clouserContractUtils.deployClouserContract(sender, recipient, permissionContractContent, web3j);
            permissionContractContent.setClosureContent(null);
        }

        try {
            log.info("wallet balance before deployment: {}", Web3jUtils.getBalanceWei(web3j, sender.getAddress()));
            Permission_sol_PermissionContract contract = Permission_sol_PermissionContract.deploy(
                    web3j,
                    sender.getCredentials(),
                    Web3jConstants.GAS_PRICE,
                    Web3jConstants.GAS_LIMIT_PERMISSION_TX,
                    recipient,
                    ObjectToString.toString(permissionContractContent),
                    clouserContractAddress
            ).send();
            if (contract == null)
                throw new NullPointerException("Permission Contract is null. Permission Contract could not be created");

            log.info("PermissionContract Address: {}, and clouserContentAddress: {}", contract.getContractAddress(), contract.getClouserContractAddress().send());
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

            String clouserAddress = contract.getClouserContractAddress().send();
            if(!clouserAddress.equals(Web3jConstants.NO_CLOSURE_ADDRESS)){
                log.info("clouser address != null ( {} ), so access the contract to get clousers", clouserAddress);
                permissionContractContent.setClosureContent(clouserContractUtils.getClouserContractContent(account,clouserAddress,web3j));
            }else{
                log.info("clouser address is not used, no clousers");
            }

            log.info("PermissionContract Address: {}, Amount NecessaryClaims: {}, Amount OptionalClaims: {} , RequesterAdress {}, ClouserAddress: {}", contract.getContractAddress(), permissionContractContent.getRequiredClaims().size(), permissionContractContent.getOptionalClaims(), permissionContractContent.getRequesterAddress(), clouserAddress);

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
                throw new NullPointerException("Permission Contract is null. Permission Contract does not exists");


            String clouserAddress = contract.getClouserContractAddress().send();
            if(!clouserAddress.equals(Web3jConstants.NO_CLOSURE_ADDRESS)){
                log.info("clouser address is set, so approved clousers have to be added");
                clouserContractUtils.setApprovedClosure(account, clouserAddress,permissionContractContent.getClosureContent(),web3j);
                permissionContractContent.setClosureContent(null);
            }else{
                log.info("clouser address is not set, no clousers have to be approved");

            }

            TransactionReceipt transactionReceipt = contract.setAndApproveClaims(ObjectToString.toString(permissionContractContent)).send();
            PermissionContractContent permissionContractContentFromContract = (PermissionContractContent) ObjectToString.fromString(contract.getClaims().send());
            log.info("PermissionContract Contract Address: {}, claims approved by user with transactionReceipt status: {} , with required claims: {} and optional claims {}", contract.getContractAddress(),transactionReceipt.getStatus(),permissionContractContentFromContract.getRequiredClaims().size(), permissionContractContentFromContract.getOptionalClaims());


        } catch (Exception e) {
            throw new EBAException(e);
        }
    }
}
