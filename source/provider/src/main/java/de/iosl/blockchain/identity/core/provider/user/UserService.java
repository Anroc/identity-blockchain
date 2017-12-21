package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.data.claim.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.data.repository.UserDB;
import de.iosl.blockchain.identity.core.provider.data.user.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        user = updateModificationDateForClaims(user);
        return userDB.insert(user);
    }

    public User updateUser(User user) {
        user = updateModificationDateForClaims(user);
        return userDB.update(user);
    }

    public void removeUser(@NonNull User user) {
        userDB.deleteUser(user.getId());
    }

    public boolean exists(@NonNull String userId) {
        return userDB.exist(userId);
    }

    public ProviderClaim createClaim(@NonNull User user, @NonNull ProviderClaim claim) {
        claim.setModificationDate(new Date());
        user.putClaim(claim);
        userDB.update(user);
        return claim;
    }

    public void removeClaim(@NonNull User user, @NonNull String claimId) {
        user.removeClaim(claimId);
        userDB.update(user);
    }

    private User updateModificationDateForClaims(@NonNull User user) {
        user.getClaims().forEach(claim -> claim.setModificationDate(new Date()));
        return user;
    }

}