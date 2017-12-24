package de.iosl.blockchain.identity.core.provider.user.db;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDB extends CouchbaseWrapper<User, String> {
    private final UserRepository userRepository;
    private final Bucket bucket;

    @Autowired
    public UserDB(UserRepository userRepository, Bucket bucket) {
        super(userRepository);
        this.userRepository = userRepository;
        this.bucket = bucket;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateOrCreateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (checkUserExists(id)) {
            userRepository.delete(id);
        }
    }

    public Optional<User> getUser(String id) {
        return Optional.of(userRepository.findOne(id));
    }

    public Optional<User> findUserByEthId(String ethId) {
        return userRepository.findByEthID(ethId);
    }

    public Optional<User> findOne(String id) {
        return Optional.of(userRepository.findOne(id));
    }

    public void addClaimToUser(String id, ProviderClaim providerClaim) {
        bucket.mutateIn(id).arrayAppend("claims", providerClaim, false).execute();
    }

    public void removeClaimFromUser(String id, ProviderClaim providerClaim) {
        Optional<User> userOptional = Optional.of(userRepository.findOne(id));
        userOptional.ifPresent(user -> {
            user.getClaims().remove(providerClaim);
            userRepository.save(user);
        });
    }

    private boolean checkUserExists(String id) {
        return userRepository.exists(id);
    }

}
