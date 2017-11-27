package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.discovery.registry.DiscoveryService;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryControllerRestTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private DiscoveryService discoveryService;

	private static AsymmetricCryptEngine<Object> ENGINE;

	private static final String ETH_ID = "ETH_ID";
	private static final String DOMAIN = "example.com";
	private static final int PORT = 3003;
	private static String PUBLIC_KEY;

	private String mac;
	private RegistryEntry registryEntry;

	@BeforeClass
	public static void init() {
		ENGINE = CryptEngine.generate().json().rsa();
		PUBLIC_KEY = KeyConverter.from(ENGINE.getPublicKey()).toBase64();
	}

	@Before
	public void setup() throws Exception {
		Payload payload = new Payload(ETH_ID, PUBLIC_KEY, DOMAIN, PORT);

		mac = ENGINE.sign(payload);
		registryEntry = new RegistryEntry(payload, mac);

		discoveryService.dropEntries();
	}

	@Test
	public void createEntryTest() {
		ResponseEntity<?> responseEntity = restTemplate.exchange("/provider", HttpMethod.POST, new HttpEntity<>(registryEntry), Object.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(discoveryService.getEntry(ETH_ID).get()).isEqualTo(registryEntry);
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
