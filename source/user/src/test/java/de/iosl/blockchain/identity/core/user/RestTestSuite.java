package de.iosl.blockchain.identity.core.user;

import de.iosl.blockchain.identity.core.shared.message.data.Message;
import de.iosl.blockchain.identity.core.shared.message.db.MessageDB;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class RestTestSuite {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected MessageDB messageDB;

    @After
    @Before
    public void capDatabase() {
        messageDB.deleteAll(Message.class);
    }
}
