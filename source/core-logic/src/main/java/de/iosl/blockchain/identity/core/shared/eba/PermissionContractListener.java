package de.iosl.blockchain.identity.core.shared.eba;

public interface PermissionContractListener {

    /**
     * Execute this method if the status of a permission contract is updated.
     * After execution this object can be destroyed. See {@link Runnable#run()}
     * for more information about this method.
     *
     * @param queryResult the result that the permission contract resulted in.
     *                    Null would indicate a rejection.
     */
    void callback(String queryResult);
}
