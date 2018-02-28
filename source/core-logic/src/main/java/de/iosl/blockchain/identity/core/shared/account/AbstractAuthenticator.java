package de.iosl.blockchain.identity.core.shared.account;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public abstract class AbstractAuthenticator {

    @Autowired
    @Getter
    private KeyChain keyChain;

    protected void checkAuthentication() {
        if(! keyChain.isActive()) {
            throw new ServiceException("User is not active. (Not logined or registered)", HttpStatus.UNAUTHORIZED);
        }
    }
}
