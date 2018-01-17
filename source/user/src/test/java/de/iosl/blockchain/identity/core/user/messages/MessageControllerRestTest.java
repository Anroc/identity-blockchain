package de.iosl.blockchain.identity.core.user.messages;

import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.RestTestSuite;
import de.iosl.blockchain.identity.core.user.messages.data.Message;
import de.iosl.blockchain.identity.core.user.messages.data.MessageType;
import de.iosl.blockchain.identity.core.user.messages.dto.MessageDTO;
import de.iosl.blockchain.identity.core.user.messages.dto.MessageUpdateDTO;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class MessageControllerRestTest extends RestTestSuite {

    private Message message;

    @Before
    public void setup() {
        message = new Message(UUID.randomUUID().toString(), MessageType.PERMISSION_REQUEST, false);
        messageDB.insert(message);
    }

    @Test
    public void getMessage() {

        ResponseEntity<List<MessageDTO>> responseEntity =
                restTemplate.exchange(
                        "/messages",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<MessageDTO>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<MessageDTO> messageDTOs = responseEntity.getBody();

        assertThat(messageDTOs).isNotEmpty().hasSize(1);
        assertThat(messageDTOs.get(0)).isEqualToIgnoringGivenFields(message);
    }

    @Test
    public void updateMessage() {
        MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO(true);

        ResponseEntity<MessageDTO> responseEntity =
                restTemplate.exchange(
                        String.format("/messages/%s", message.getId()),
                        HttpMethod.PUT,
                        new HttpEntity<>(messageUpdateDTO),
                        MessageDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().isSeen()).isTrue();
    }

}
