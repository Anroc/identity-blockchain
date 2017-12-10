package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.discovery.data.ECSignature;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.registry.DiscoveryService;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import org.junit.Before;
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
import org.web3j.crypto.Sign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryControllerRestTest extends RestTestSuite {

    private static final String DOMAIN = "example.com";
    private static final int PORT = 3003;
    @Autowired
    private DiscoveryService discoveryService;
    private RequestDTO<RegistryEntryDTO> requestDTO;

    @Before
    public void setup() {
        RegistryEntryDTO payload = new RegistryEntryDTO(ETH_ID, PUBLIC_KEY, DOMAIN, PORT);

        Sign.SignatureData signature = ALGORITHM
                .sign(payload, credentials.getEcKeyPair());
        ECSignature ecSignature = ECSignature.fromSignatureData(signature);

        requestDTO = new RequestDTO<>(payload, ecSignature);
    }

    @Test
    public void createEntryTest() {
        ResponseEntity<?> responseEntity = restTemplate
                .exchange("/provider", HttpMethod.POST,
                        new HttpEntity<>(requestDTO), Object.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(registryEntryDB.findEntity(ETH_ID).get())
                .isEqualToIgnoringGivenFields(
                        fromRegisterEntryDTO(requestDTO),
                        "creationDate", "lastModified", "version", "lastSeen");
    }

    @Test
    public void createEntryFailsWithForbidden() {
        requestDTO.getPayload().setEthID("OTHER_VALUE");

        ResponseEntity<?> responseEntity = restTemplate
                .exchange("/provider", HttpMethod.POST,
                        new HttpEntity<>(requestDTO), Object.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getEntryTest() {
        discoveryService.putEntry(fromRegisterEntryDTO(requestDTO));

        ResponseEntity<?> responseEntity = restTemplate
                .getForEntity("/provider/" + ETH_ID, RegistryEntry.class);

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(registryEntryDB.findEntity(ETH_ID).get())
                .isEqualToIgnoringGivenFields(
                        fromRegisterEntryDTO(requestDTO),
                        "creationDate", "lastModified", "version", "lastSeen");
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
        registryEntryDB.insert(fromRegisterEntryDTO(requestDTO));

        RequestDTO otherEntryDTO = new RequestDTO(
                new RegistryEntryDTO(
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

        registryEntryDB.insert(fromRegisterEntryDTO(otherEntryDTO));

        ResponseEntity<List<RequestDTO<RegistryEntryDTO>>> responseEntity = restTemplate
                .exchange(
                        "/provider",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<RequestDTO<RegistryEntryDTO>>>() {
                        }
                );

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
        assertThat(responseEntity.getBody())
                .containsExactlyInAnyOrder(requestDTO, otherEntryDTO);
    }

    @Test
    public void getEntriesWithQueryParameterTest() {
        discoveryService.putEntry(fromRegisterEntryDTO(requestDTO));

        RegistryEntry otherEntry = new RegistryEntry(
                new RegistryEntryDTO(
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
        ResponseEntity<List<RequestDTO<RegistryEntryDTO>>> responseEntity = restTemplate
                .exchange(
                        "/provider?domainName=" + DOMAIN,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<RequestDTO<RegistryEntryDTO>>>() {
                        }
                );

        assertThat(responseEntity.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getBody().get(0)).isEqualTo(requestDTO);
    }

    private static RegistryEntry fromRegisterEntryDTO(RequestDTO<RegistryEntryDTO> requestDTO) {
        return new RegistryEntry(
                requestDTO.getPayload(),
                requestDTO.getSignature(),
                requestDTO.getPayload().getEthID()
        );
    }
}
