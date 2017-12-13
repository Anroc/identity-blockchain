package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.discovery.data.ECSignature;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.HeartBeatRequest;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Sign;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeartBeatControllerRestTest extends RestTestSuite {

    private static final String ENDPOINT = "/claims/ETHEREM_ID";
    private static final String OTHER_ETH_ID = "other_eth_id";

    private RequestDTO<HeartBeatRequest> requestDTO;

    @Before
    public void setup() {
        HeartBeatRequest payload = new HeartBeatRequest(ETH_ID, ENDPOINT);

        Sign.SignatureData signature = ALGORITHM
                .sign(payload, credentials.getEcKeyPair());
        ECSignature ecSignature = ECSignature.fromSignatureData(signature);

        requestDTO = new RequestDTO<>(payload, ecSignature);
    }

    @Before
    @After
    public void cleanUp() {
        if(beatDB.exist(beatDB.createCounterID(OTHER_ETH_ID))) {
            beatDB.deleteCounter(OTHER_ETH_ID);
        }
    }


    @Test
    public void createAndGetMessageTest() {
        RegistryEntry registryEntry = new RegistryEntry(
                OTHER_ETH_ID,
                null,
                null,
                null,
                new Date(),
                null,
                null
        );

        registryEntryDB.insert(registryEntry);

        ResponseEntity<Beat> responseEntity = restTemplate
                .exchange("/heartbeat/" + OTHER_ETH_ID, HttpMethod.POST,
                        new HttpEntity<>(requestDTO), Beat.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(beatDB.findEntity(Beat.buildID(OTHER_ETH_ID, 0L)).get()).isEqualTo(responseEntity.getBody());

        ResponseEntity<List<Beat>> listResponse = restTemplate
                .exchange(
                        "/heartbeat/" + OTHER_ETH_ID,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<Beat>>() {}
                );

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(1);
        assertThat(beatDB.findEntity(Beat.buildID(OTHER_ETH_ID, 0L)).get()).isEqualTo(listResponse.getBody().get(0));

    }

    @Test
    public void heartbeatTest() {
        RegistryEntry registryEntry = new RegistryEntry(
                OTHER_ETH_ID,
                null,
                null,
                null,
                new Date(),
                null,
                null
        );

        registryEntryDB.insert(registryEntry);

        ResponseEntity<List<Beat>> listResponse = restTemplate
                .exchange(
                        "/heartbeat/" + OTHER_ETH_ID,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<Beat>>() {}
                );

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(0);
    }


}
