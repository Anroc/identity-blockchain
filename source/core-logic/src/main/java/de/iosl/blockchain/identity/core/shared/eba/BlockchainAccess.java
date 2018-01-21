package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.AccountAccess;
import de.iosl.blockchain.identity.core.shared.eba.main.RegistrarContractUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@Slf4j
@Data
@Component
public class BlockchainAccess implements EBAInterface {

    @Autowired
    private AccountAccess accountAccess;

    @Autowired
    private RegistrarContractUtils registrarContractUtils;

    @Autowired
    private Web3j web3j;

    @Bean
    public Web3j buildHttpClientByParams(BlockchainIdentityConfig config) {
        String url = String.format("http://%s:%s", config.getEthereum().getAddress(), config.getEthereum().getPort());
        log.debug(url);
        return Web3j.build(new HttpService(url));
    }

    @Override
    public Account createWallet(@NonNull String password, Path path) {
        return accountAccess.createAccount(password, path, web3j);
    }

    @Override
    public Account accessWallet(String password, File file) {
        return accountAccess.accessWallet(password, file);
    }

    @Override
    public String deployRegistrarContract(Account account){
        return registrarContractUtils.deployRegistrarContract(account, web3j);
    }

    @Override
    public void setRegisterApproval(Account governmentAccount, String contractAddress, boolean decision) {
        this.registrarContractUtils.approveRegistrarContractAsGovernment(governmentAccount,contractAddress,decision, web3j);
    }

    @Override
    public boolean getRegisterApproval(Account account, String contractAddress){
        return this.registrarContractUtils.getApprovalByContractAdress(account, contractAddress, web3j);
    }

    @Override
    public String deployPermissionContract(Account sender, String recipient, String requesterAddress, Set<String> requiredClaims, Set<String> optionalClaims) {
        // TODO: implement
        return null;
    }

    @Override
    public PermissionContractContent getPermissionContractContent(Account account, String smartContractAddress) {
        // TODO: implement
        return null;
    }


    public void approvePermissionContract(Account account, String smartContractAddress, PermissionContractContent permissionContractContent) {
        // TODO: implement
    }
}
