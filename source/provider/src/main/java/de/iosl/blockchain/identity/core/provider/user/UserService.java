package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.user.db.UserDB;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.EventType;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDB userDB;
    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private EBAInterface ebaInterface;
    @Autowired
    private KeyChain keyChain;

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

    public List<User> search(String givenName, String familyName) {
        // TODO: refactore
        return userDB.findAll()
                .stream()
                .filter(user -> filterForClaimAttribute(user, givenName))
                .filter(user -> filterForClaimAttribute(user, familyName))
                .collect(Collectors.toList());
    }

    private boolean filterForClaimAttribute(User user, String claimValue) {
        return user.getClaims()
                .stream()
                .filter(claim -> claim.getClaimValue().getPayload().equals(claimValue))
                .findAny()
                .isPresent();
    }

    private User updateModificationDateForClaims(@NonNull User user) {
        user.getClaims().forEach(claim -> claim.setModificationDate(new Date()));
        return user;
    }

    public void registerUser(@NonNull User user) {
        log.info("Registering user: {} ({})", user.getId(), user.getEthId());
        updateUser(user);
        ebaInterface.setApproval(keyChain.getAccount(), user.getRegisterContractAddress(), true);
        heartBeatService.createBeat(user.getEthId(), EventType.NEW_CLAIMS);
        log.info("Registered user: {} ({})", user.getId(), user.getEthId());
    }
}