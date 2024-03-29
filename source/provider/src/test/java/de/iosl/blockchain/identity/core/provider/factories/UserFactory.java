package de.iosl.blockchain.identity.core.provider.factories;

import com.google.common.collect.Sets;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import org.assertj.core.util.Lists;

import java.util.UUID;

public class UserFactory {

    private ClaimFactory claimFactory = ClaimFactory.instance();

    public User create() {
        return new User(UUID.randomUUID().toString(),
                "some_publicKey_" + UUID.randomUUID().toString(),
                "some_eth_id" + UUID.randomUUID().toString(),
                "0x88123871387123",
                Lists.newArrayList(),
                Sets.newHashSet(claimFactory.create())
                );
    }

    public static UserFactory instance() {
        return new UserFactory();
    }
}
