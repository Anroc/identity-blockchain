package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
import de.iosl.blockchain.identity.core.user.claims.repository.UserClaimDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {

    @Autowired
    private UserClaimDB claimDB;
    @Autowired
    private KeyChain keyChain;

    public List<UserClaim> getClaims() {
        return claimDB.findAllByEthID(keyChain.getAccount().getAddress());
    }
}
