package de.iosl.blockchain.identity.core.shared.eba;

import com.google.common.collect.Sets;
import com.sun.org.apache.regexp.internal.RE;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.iosl.blockchain.identity.core.shared.KeyChain.WALLET_DIR;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PermissionContractTest extends RestTestSuite{


    @Autowired
    private BlockchainAccess blockchainAccess;

    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final Set<String> requiredClaims = Sets.newHashSet(claimID_givenName, claimID_familyName);
    private final Set<String> optionalClaims = Sets.newHashSet(claimID_age);

    private final String govWalletName = "gov-wallet.json";
    private final String govPassword = "penispumpe";

    private final String userWallet = RestTestSuite.USER_FILE;
    private final String userPassword = RestTestSuite.WALLET_PW;
    private final String providerWallet = RestTestSuite.PROVIDER_FILE;
    private final String providerPassword = RestTestSuite.WALLET_PW;
    private PermissionContractContent permissionContractContent;
    private Account governmentAccount;
    private Account userAccount;
    private Account providerAccount;


    @Before
    public void setUp() throws Exception{

        Credentials govCred = RestTestSuite.loadWallet(govWalletName, govPassword);
        this.governmentAccount =new Account (
                govCred.getAddress(),
                govCred.getEcKeyPair().getPublicKey(),
                govCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(govWalletName),
                govCred
        );


        Credentials userCred = RestTestSuite.loadWallet(userWallet, userPassword);
        this.userAccount =new Account (
                govCred.getAddress(),
                govCred.getEcKeyPair().getPublicKey(),
                govCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(govWalletName),
                govCred
        );

        Credentials providerCred = RestTestSuite.loadWallet(providerWallet,providerPassword);
        this.providerAccount =new Account (
                govCred.getAddress(),
                govCred.getEcKeyPair().getPublicKey(),
                govCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(govWalletName),
                govCred
        );

        this.permissionContractContent= new PermissionContractContent(requiredClaims, optionalClaims, providerAccount.getAddress());
    }

    @Test
    public void deployPermissionContract() throws Exception {
        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), providerAccount.getAddress(), requiredClaims, optionalClaims);
        assertThat(permissionContractAddress).isNotNull();
    }


    @Test
    public void rightClaimsSetInContractAsGovernmentForProviderTest() throws  Exception{
        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), providerAccount.getAddress(), requiredClaims, optionalClaims);
        PermissionContractContent permissionContractContent= blockchainAccess.getPermissionContractContent(providerAccount, permissionContractAddress);

        permissionContractContent.getRequiredClaims().entrySet().stream().forEach(entry -> {
            log.info("Claim: "+entry.getKey());
            log.info("Claim value: "+entry.getValue());
            assertThat(requiredClaims.contains(entry.getKey()));
            assertThat(entry.getValue()==null);
        });

        permissionContractContent.getOptionalClaims().entrySet().stream().forEach(entry -> {
            log.info("Claim: "+entry.getKey());
            log.info("Claim value: "+entry.getValue());
            assertThat(optionalClaims.contains(entry.getKey()));
            assertThat(entry.getValue()==null);
        });
    }

    @Test
    public void setApprovedClaimsAsUserInContractTest() throws Exception{

        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), providerAccount.getAddress(), requiredClaims, optionalClaims);
        PermissionContractContent permissionContractContent= blockchainAccess.getPermissionContractContent(providerAccount, permissionContractAddress);

        String name = "Isol";
        String familyName="MiTiMaOs";
        String age = "25";
        permissionContractContent.getRequiredClaims().put(claimID_givenName, name);
        permissionContractContent.getRequiredClaims().put(claimID_familyName, familyName);
        permissionContractContent.getOptionalClaims().put(claimID_age, age);

        PermissionContractContent permissionContractContentCompare = new PermissionContractContent(permissionContractContent.getRequiredClaims(), permissionContractContent.getOptionalClaims(), providerAccount.getAddress());

        permissionContractContent.getRequiredClaims().entrySet().stream().forEach(entry -> {
            log.info("Claim: "+entry.getKey());
            log.info("Claim value: "+entry.getValue());
            assertThat(requiredClaims.contains(entry.getKey()));
            assertThat(permissionContractContentCompare.getRequiredClaims().get(entry.getKey()).equals(entry.getValue()));
        });

        permissionContractContent.getOptionalClaims().entrySet().stream().forEach(entry -> {
            log.info("Claim: "+entry.getKey());
            log.info("Claim value: "+entry.getValue());
            assertThat(optionalClaims.contains(entry.getKey()));
            assertThat(permissionContractContentCompare.getOptionalClaims().get(entry.getKey()).equals(entry.getValue()));
        });
    }
}
