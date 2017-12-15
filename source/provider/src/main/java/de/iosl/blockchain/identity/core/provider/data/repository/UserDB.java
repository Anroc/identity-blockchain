package de.iosl.blockchain.identity.core.provider.data.repository;

import com.couchbase.client.java.Bucket;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findOne(String id){
        return userRepository.findOne(id);
    }
}
