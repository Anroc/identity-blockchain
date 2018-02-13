package de.iosl.blockchain.identity.core.shared.eba;

import com.google.common.collect.Sets;
import com.sun.org.apache.regexp.internal.RE;
import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.shared.api.permission.ClosureContentCryptEngine;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequestPayload;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PermissionContractTest extends RestTestSuite{

    private static final int MAX_NUMBER_OF_CLOSURES = 5;

    @Autowired
    private BlockchainAccess blockchainAccess;

    @Autowired
    private ClosureContentCryptEngine closureContentCryptEngine;

    private final String claimID_givenName = "GIVEN_NAME";
    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final Set<String> requiredClaims = Sets.newHashSet(claimID_givenName, claimID_familyName);
    private final Set<String> optionalClaims = Sets.newHashSet(claimID_age);

    private final String govWalletName = RestTestSuite.STATE_FILE;
    private final String govPassword = RestTestSuite.WALLET_PW;

    private final String userWallet = RestTestSuite.USER_FILE;
    private final String userPassword = RestTestSuite.WALLET_PW;
    private final String providerWallet = RestTestSuite.PROVIDER_FILE;
    private final String providerPassword = RestTestSuite.WALLET_PW;
    private PermissionContractContent permissionContractContent;
    private Account governmentAccount;
    private Account userAccount;
    private Account providerAccount;

    private static String PUBLIC_KEY;

    @BeforeClass
    public static void init() {
        PUBLIC_KEY = new KeyConverter().from(CryptEngine.generate().string().rsa().getPublicKey()).toBase64();
    }

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
                userCred.getAddress(),
                userCred.getEcKeyPair().getPublicKey(),
                userCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(userWallet),
                userCred
        );

        Credentials providerCred = RestTestSuite.loadWallet(providerWallet,providerPassword);
        this.providerAccount =new Account (
                providerCred.getAddress(),
                providerCred.getEcKeyPair().getPublicKey(),
                providerCred.getEcKeyPair().getPublicKey(),
                RestTestSuite.loadFile(providerWallet),
                govCred
        );
        ClosureContent closureContent = buildClosureContent(providerCred);

        this.permissionContractContent= new PermissionContractContent(requiredClaims, optionalClaims, providerAccount.getAddress(), closureContent);
    }

    private ClosureContent buildClosureContent(Credentials providerCred) {
        EthereumSigner ethereumSigner = new EthereumSigner();
        Set<ClosureContractRequest> set = new HashSet<>();
        for(int i = 0; i < MAX_NUMBER_OF_CLOSURES; i++) {
            ClosureContractRequestPayload payload1 = new ClosureContractRequestPayload("0x123", "GIVEN_NAME",
                    ClaimOperation.EQ, new ValueHolder("Hans" + i));
            ClosureContractRequest ccr = new ClosureContractRequest(
                    payload1,
                    ECSignature.fromSignatureData(ethereumSigner.sign(payload1, providerCred.getEcKeyPair()))
            );
            set.add(ccr);
            log.info("Generating {}: {} ",i, ccr);
        }
        return closureContentCryptEngine.encrypt(PUBLIC_KEY, set);
    }

    @Test
    public void deployPermissionContract() throws Exception {
        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), permissionContractContent);
        assertThat(permissionContractAddress).isNotNull();
    }


    @Test
    public void rightClaimsSetInContractAsGovernmentForProviderTest() throws  Exception{
        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), permissionContractContent);
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
        PermissionContractContent usedPermissionContractContent = this.permissionContractContent;

        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), usedPermissionContractContent);
        PermissionContractContent permissionContractContent= blockchainAccess.getPermissionContractContent(userAccount, permissionContractAddress);

        String name = "Isol";
        String familyName="MiTiMaOs";
        String age = "25";
        permissionContractContent.getRequiredClaims().put(claimID_givenName, name);
        permissionContractContent.getRequiredClaims().put(claimID_familyName, familyName);
        permissionContractContent.getOptionalClaims().put(claimID_age, age);

        PermissionContractContent permissionContractContentCompare = new PermissionContractContent(permissionContractContent.getRequiredClaims(), permissionContractContent.getOptionalClaims(), providerAccount.getAddress(), null);

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

    @Test
    public void setClosureAsUserInContractAndGetAsProviderTest() throws Exception{
        PermissionContractContent usedPermissionContractContent = this.permissionContractContent;

        String permissionContractAddress= blockchainAccess.deployPermissionContract(governmentAccount, userAccount.getAddress(), usedPermissionContractContent);
        PermissionContractContent permissionContractContent= blockchainAccess.getPermissionContractContent(userAccount, permissionContractAddress);

        String name = "Isol";
        String familyName="MiTiMaOs";
        String age = "25";
        permissionContractContent.getRequiredClaims().put(claimID_givenName, name);
        permissionContractContent.getRequiredClaims().put(claimID_familyName, familyName);
        permissionContractContent.getOptionalClaims().put(claimID_age, age);


        PermissionContractContent approvePermissionContractContentCompare = new PermissionContractContent(permissionContractContent.getRequiredClaims(), permissionContractContent.getOptionalClaims(), providerAccount.getAddress(),permissionContractContent.getClosureContent());
        blockchainAccess.approvePermissionContract(userAccount,permissionContractAddress, approvePermissionContractContentCompare);

        //provider gets approved once from contract
        log.info("provider gets approved once from contract");
        PermissionContractContent permissionContractContentCompare =blockchainAccess.getPermissionContractContent(providerAccount, permissionContractAddress);
        log.info("Provider get appr");


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

        permissionContractContent.getClosureContent().getEncryptedRequests().stream().forEach(encryptedClosure->{
            log.info("closure: "+encryptedClosure);
            assertThat(permissionContractContent.getClosureContent().getEncryptedRequests().contains(encryptedClosure));
        });
    }


}
