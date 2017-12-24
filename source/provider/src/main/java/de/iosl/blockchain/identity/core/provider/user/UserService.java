package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.user.db.UserDB;
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

    public Optional<User> findUserByEthID(@NonNull String ethId) {
        return userDB.findUserByEthId(ethId);
    }

    public User insertUser(@NonNull User user) {
        return userDB.insert(user);
    }

    public User updateUser(User user) {
        return userDB.update(user);
    }

    public void removeUser(@NonNull User user) {
        userDB.deleteUser(user.getId());
    }

    public boolean exists(@NonNull String userId) {
        return userDB.exist(userId);
    }

    public ProviderClaim createClaim(@NonNull User user, @NonNull ProviderClaim claim) {
        user.putClaim(claim);
        userDB.update(user);
        return claim;
    }

    public void removeClaim(@NonNull User user, @NonNull String claimId) {
        user.removeClaim(claimId);
        userDB.update(user);
    }


}