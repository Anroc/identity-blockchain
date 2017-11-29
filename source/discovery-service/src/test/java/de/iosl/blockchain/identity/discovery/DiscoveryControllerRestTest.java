package de.iosl.blockchain.identity.discovery;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.DiscoveryService;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryControllerRestTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private DiscoveryService discoveryService;

	private static final String FILE = "sample_wallet.json";
	private static final String DOMAIN = "example.com";
	private static final int PORT = 3003;
	private static String ETH_ID;
	private static String PUBLIC_KEY;
	private static Credentials credentials;

	private static EthereumSigner ALGORITHM;

	private RegistryEntry registryEntry;

	@BeforeClass
	public static void init() throws Exception {
		ClassPathResource resource = new ClassPathResource(FILE);
		File file = resource.getFile();

		credentials = WalletUtils.loadCredentials("asd", file);

		ETH_ID = Numeric.prependHexPrefix(Keys.getAddress(credentials.getEcKeyPair().getPublicKey()));
		PUBLIC_KEY = "SOME_PUBLIC_KEY";

		ALGORITHM = new EthereumSigner();
	}

	@Before
	public void setup() throws Exception {
		Payload payload = new Payload(ETH_ID, PUBLIC_KEY, DOMAIN, PORT);

		Sign.SignatureData signature = ALGORITHM.sign(payload, credentials.getEcKeyPair());
		ECSignature ecSignature = ECSignature.fromSignatureData(signature);

		registryEntry = new RegistryEntry(payload, ecSignature);

		discoveryService.dropEntries();
	}

	@Test
	public void createEntryTest() {
		ResponseEntity<?> responseEntity = restTemplate.exchange("/provider", HttpMethod.POST, new HttpEntity<>(registryEntry), Object.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(discoveryService.getEntry(ETH_ID).get()).isEqualTo(registryEntry);
	}

	@Test
	public void createEntryFailsWithForbidden() {
		registryEntry.getPayload().setEthID("OTHER_VALUE");

		ResponseEntity<?> responseEntity = restTemplate.exchange("/provider", HttpMethod.POST, new HttpEntity<>(registryEntry), Object.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
	}

	@Test
	public void getEntryTest() {
		discoveryService.putEntry(registryEntry);

		ResponseEntity<?> responseEntity = restTemplate.getForEntity("/provider/" + ETH_ID, RegistryEntry.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(discoveryService.getEntry(ETH_ID).get()).isEqualTo(registryEntry);
	}

	@Test
	public void getEntryNotFoundTest() {
		ResponseEntity<?> responseEntity = restTemplate.getForEntity("/provider/" + ETH_ID, RegistryEntry.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}
}
