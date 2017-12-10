package de.iosl.blockchain.identity.discovery;

import de.iosl.blockchain.identity.discovery.data.ECSignature;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Message;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.MessageRequest;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.web3j.crypto.Sign;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HearthBeatControllerRestTest extends RestTestSuite {

    private static final String ENDPOINT = "/claim/ETHEREM_ID";
    private static final String OTHER_ETH_ID = "other_eth_id";

    private RequestDTO<MessageRequest> requestDTO;

    @Before
    public void setup() {
        MessageRequest payload = new MessageRequest(ETH_ID, ENDPOINT);

        Sign.SignatureData signature = ALGORITHM
                .sign(payload, credentials.getEcKeyPair());
        ECSignature ecSignature = ECSignature.fromSignatureData(signature);

        requestDTO = new RequestDTO<>(payload, ecSignature);
    }

    @Before
    @After
    public void cleanUp() {
        if(messageDB.exist(messageDB.createCounterID(OTHER_ETH_ID))) {
            messageDB.deleteCounter(OTHER_ETH_ID);
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

        ResponseEntity<Message> responseEntity = restTemplate
                .exchange("/heartbeat/" + OTHER_ETH_ID, HttpMethod.POST,
                        new HttpEntity<>(requestDTO), Message.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(messageDB.findEntity(Message.buildID(OTHER_ETH_ID, 0L)).get()).isEqualTo(responseEntity.getBody());

        ResponseEntity<List<Message>> listResponse = restTemplate
                .exchange(
                        "/heartbeat/" + OTHER_ETH_ID,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<Message>>() {}
                );

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(1);
        assertThat(messageDB.findEntity(Message.buildID(OTHER_ETH_ID, 0L)).get()).isEqualTo(listResponse.getBody().get(0));

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

        ResponseEntity<List<Message>> listResponse = restTemplate
                .exchange(
                        "/heartbeat/" + OTHER_ETH_ID,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<Message>>() {}
                );

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(0);
    }


}
