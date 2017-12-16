package de.iosl.blockchain.identity.core.provider.data.repository;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.core.shared.claims.claim.Claim;
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

    public User findUserByPublicKey(String publicKey) {
        if (userRepository.findAll().size() > 0) {
            for (User user : userRepository.findAll()) {
                if (user.getPublicKey().equals(publicKey)) {
                    return user;
                }
            }
        }
        return null;
    }

    public Optional<User> findOne(String id) {
        return Optional.of(userRepository.findOne(id));
    }

    public void addClaimToUser(String id, Claim claim) {
        if (checkUserExists(id)) {
            User user = userRepository.findOne(id);
            user.getClaimList().add(claim);
            updateOrCreateUser(user);
        }
    }

    public void removeClaimFromUser(String id, Claim claim) {
        if (checkUserExists(id)) {
            User user = userRepository.findOne(id);
            user.getClaimList().remove(claim);
            updateOrCreateUser(user);
        }
    }

    private Boolean checkUserExists(String id) {
        return userRepository.exists(id);
    }

}
