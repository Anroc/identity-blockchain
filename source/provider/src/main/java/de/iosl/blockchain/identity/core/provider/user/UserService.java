package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.data.repository.UserDB;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDB userDB;

    public List<User> getUsers() {
        return userDB.findAll();
    }

    public Optional<User> findUser(@NonNull String id) {
        return userDB.findEntity(id);
    }

    public User insertUser(@NonNull User user) {
        return userDB.insert(user);
    }
}