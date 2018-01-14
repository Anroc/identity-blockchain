package de.iosl.blockchain.identity.core.provider.api;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.provider.Application;
import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.InfoDTO;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ProviderAPIRestTest extends RestTestSuite {

    private static Credentials USER_CREDENTIALS;

    private User user;

    @Autowired
    private ProviderConfig config;

    @BeforeClass
    public static void setup() throws IOException, CipherException {
        USER_CREDENTIALS = loadWallet(USER_FILE, WALLET_PW);
    }

    @Before
    public void init() {
        user = userFactory.create();
        user.setEthId(USER_CREDENTIALS.getAddress());
        user.setPublicKey(KeyConverter.from("This ain't no key".getBytes()).toBase64());

        userDB.insert(user);
    }

    @Test
    public void getClaimsTest() {
        ProviderClaim providerClaim = claimFactory.create("SPECIAL_CLAIM_ID");
        user.putClaim(providerClaim);

        userDB.update(user);

        BasicEthereumDTO basicEthereumDTO = new BasicEthereumDTO(USER_CREDENTIALS.getAddress());

        SignedRequest<BasicEthereumDTO> claimRequest = new SignedRequest<>(
                basicEthereumDTO,
                getSignature(basicEthereumDTO, USER_CREDENTIALS)
        );

        ResponseEntity<List<ClaimDTO>> responseEntity = restTemplate.exchange(
                ProviderAPIConstances.ABSOLUTE_CLAIM_ATH,
                HttpMethod.POST,
                new HttpEntity<>(claimRequest),
                new ParameterizedTypeReference<List<ClaimDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains(new ClaimDTO(providerClaim));
    }

    @Test
    public void infoTest() {
        InfoDTO expected = new InfoDTO(config.getBuildVersion(), config.getApiVersion(), config.getApplicationName());

        ResponseEntity<InfoDTO> responseEntity = restTemplate.getForEntity(
                ProviderAPIConstances.ABSOLUTE_INFO_PATH,
                InfoDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expected);
    }
}
