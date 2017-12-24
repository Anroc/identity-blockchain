package de.iosl.blockchain.identity.core.factories;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.provider.user.data.User;

import java.util.UUID;

public class UserFactory {

    private ClaimFactory claimFactory = ClaimFactory.instance();

    public User create() {
        return new User(UUID.randomUUID().toString(),
                "some_publicKey_" + UUID.randomUUID().toString(),
                "some_eth_id" + UUID.randomUUID().toString(),
                Sets.newHashSet(claimFactory.create())
                );
    }

    public static UserFactory instance() {
        return new UserFactory();
    }
}
