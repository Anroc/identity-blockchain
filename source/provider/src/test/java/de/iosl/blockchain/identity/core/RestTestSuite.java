package de.iosl.blockchain.identity.core;

import de.iosl.blockchain.identity.core.provider.config.CredentialConfig;
import de.iosl.blockchain.identity.core.provider.data.repository.UserDB;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import org.bouncycastle.util.encoders.Base64;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class RestTestSuite {

    @Autowired
    public TestRestTemplate restTemplate;
    @Autowired
    protected UserDB userDB;
    @Autowired
    private CredentialConfig credentialConfig;

    public String getAuthentication() {
        return "Basic " +
                new String(Base64.encode((credentialConfig.getUsername() + ":" + credentialConfig.getPassword()).getBytes()));
    }

    @After
    @Before
    public void capDatabase() {
        userDB.deleteAll(User.class);
    }
}
