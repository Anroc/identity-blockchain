package de.iosl.blockchain.identity.core.shared.eba.main.util;

import de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import org.web3j.protocol.Web3j;

public class ClouserContractUtils {

    public String deployClouserContract(Account sender, String recipient, PermissionContractContent permissionContractContent, Web3j web3j) {
        return null;
    }

    public PermissionContractContent getPermissionContractContent(Account account, String smartContractAddress, Web3j web3j) {
        return null;
    }

    public void setApprovedClaims(Account account, String smartContractAddress, PermissionContractContent permissionContractContent, Web3j web3j) {
    }
}
