package de.iosl.blockchain.identity.core.user.ethereum;

import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {

    @Autowired
    private EBAInterface ebaInterface;
    @Autowired
    private KeyChain keyChain;

    public boolean getApprovalOfRegisterContract() {
        return ebaInterface.getRegisterApproval(keyChain.getAccount(), keyChain.getRegisterSmartContractAddress());
    }

}
