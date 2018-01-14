package de.iosl.blockchain.identity.core.shared.eba;

import java.util.Map;

public interface PermissionContractListener {

    /**
     * Execute this method if the status of a permission contract is updated.
     * After execution this object can be destroyed. See {@link Runnable#run()}
     * for more information about this method.
     *
     * @param claimResult the result that the permission contract resulted in.
     *                    Empty map would indicate a rejection.
     */
    void callback(Map<String, String> claimResult);
}
