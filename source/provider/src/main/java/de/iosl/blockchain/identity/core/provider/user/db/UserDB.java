package de.iosl.blockchain.identity.core.provider.user.db;

import com.couchbase.client.java.error.DocumentDoesNotExistException;
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

    @Autowired
    public UserDB(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateOrCreateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (exist(id)) {
            userRepository.delete(id);
        }
    }

    public Optional<User> findUserByEthId(String ethId) {
        return userRepository.findByEthID(ethId);
    }

    public void addClaimToUser(String id, ProviderClaim providerClaim) {
        Optional<User> userOptional = Optional.of(userRepository.findOne(id));
        if(! userOptional.isPresent()) {
            throw new DocumentDoesNotExistException("Document with id " + id + "does not exist");
        }
        userOptional.ifPresent(user -> {
            user.getClaims().add(providerClaim);
            userRepository.save(user);
        });
    }

    public void removeClaimFromUser(String id, ProviderClaim providerClaim) {
        Optional<User> userOptional = Optional.of(userRepository.findOne(id));
        userOptional.ifPresent(user -> {
            user.getClaims().remove(providerClaim);
            userRepository.save(user);
        });
    }
}
