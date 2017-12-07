package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.DiscoveryService;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.discovery.registry.repository.RegistryEntryDB;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryControllerRestTest {

    private static final String FILE = "sample_wallet.json";
    private static final String DOMAIN = "example.com";
    private static final int PORT = 3003;
    private static String ETH_ID;
    private static String PUBLIC_KEY;
    private static Credentials credentials;
    private static EthereumSigner ALGORITHM;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DiscoveryService discoveryService;
    @Autowired
    private RegistryEntryDB registryEntryDB;
    private RegistryEntryDTO registryEntryDTO;

    @BeforeClass
    public static void init() throws Exception {
        ClassPathResource resource = new ClassPathResource(FILE);
        File file = resource.getFile();

        credentials = WalletUtils.loadCredentials("asd", file);

        ETH_ID = Numeric.prependHexPrefix(
                Keys.getAddress(credentials.getEcKeyPair().getPublicKey()));
        PUBLIC_KEY = "SOME_PUBLIC_KEY";

        ALGORITHM = new EthereumSigner();
    }

    @After
    @Before
    public void capDatabase() {
        registryEntryDB.deleteAll(RegistryEntry.class);
    }

    @Before
    public void setup() {
        Payload payload = new Payload(ETH_ID, PUBLIC_KEY, DOMAIN, PORT);

        Sign.SignatureData signature = ALGORITHM
                .sign(payload, credentials.getEcKeyPair());
        ECSignature ecSignature = ECSignature.fromSignatureData(signature);

        registryEntryDTO = new RegistryEntryDTO(payload, ecSignature);
    }

    @Test
    public void createEntryTest() {
        ResponseEntity<?> responseEntity = restTemplate
                .exchange("/provider", HttpMethod.POST,
                        new HttpEntity<>(registryEntryDTO), Object.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(registryEntryDB.findEntity(ETH_ID).get())
                .isEqualToIgnoringNullFields(
                        registryEntryDTO.toRegistryEntry());
    }

    @Test
    public void createEntryFailsWithForbidden() {
        registryEntryDTO.getPayload().setEthID("OTHER_VALUE");

        ResponseEntity<?> responseEntity = restTemplate
                .exchange("/provider", HttpMethod.POST,
                        new HttpEntity<>(registryEntryDTO), Object.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getEntryTest() {
        discoveryService.putEntry(registryEntryDTO.toRegistryEntry());

        ResponseEntity<?> responseEntity = restTemplate
                .getForEntity("/provider/" + ETH_ID, RegistryEntry.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(registryEntryDB.findEntity(ETH_ID).get())
                .isEqualToIgnoringNullFields(
                        registryEntryDTO.toRegistryEntry());
    }

    @Test
    public void getEntryNotFoundTest() {
        ResponseEntity<?> responseEntity = restTemplate
                .getForEntity("/provider/" + ETH_ID, RegistryEntry.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getEntriesTest() {
        registryEntryDB.upsert(registryEntryDTO.toRegistryEntry());

        RegistryEntryDTO otherEntryDTO = new RegistryEntryDTO(
                new Payload(
                        "asd",
                        "asd",
                        "bagutette.management",
                        3
                ),
                new ECSignature(
                        "r",
                        "s",
                        (byte) 8
                )
        );

        registryEntryDB.upsert(otherEntryDTO.toRegistryEntry());

        ResponseEntity<List<RegistryEntryDTO>> responseEntity = restTemplate
                .exchange(
                        "/provider",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<RegistryEntryDTO>>() {
                        }
                );

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
        assertThat(responseEntity.getBody())
                .containsExactlyInAnyOrder(registryEntryDTO, otherEntryDTO);
    }

    @Test
    public void getEntriesWithQueryParameterTest() {
        discoveryService.putEntry(registryEntryDTO.toRegistryEntry());

        RegistryEntry otherEntry = new RegistryEntry(
                new Payload(
                        "asd",
                        "asd",
                        "bagutette.management",
                        3
                ),
                new ECSignature(
                        "r",
                        "s",
                        (byte) 8
                ),
                "asd"
        );

        discoveryService.putEntry(otherEntry);

        Map<String, String> maps = new HashMap<>();
        maps.put("domainName", DOMAIN);
        ResponseEntity<List<RegistryEntryDTO>> responseEntity = restTemplate
                .exchange(
                        "/provider?domainName=" + DOMAIN,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<RegistryEntryDTO>>() {
                        }
                );

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getBody().get(0)).isEqualTo(registryEntryDTO);
    }
}
