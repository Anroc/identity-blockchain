package de.iosl.blockchain.identity.core.shared.eba;

import java.util.Map;

public interface PermissionContractListener {

    /**
     * Execute this method if the status of a permission contract is updated.
     * After execution this object can be destroyed. See {@link Runnable#run()}
     * for more information about this method.
     *
     * This maps consist of:
     * <li>
     * <ul>Key is the claimID.</ul>
     * <ul>Value is the base64 encoded {@link de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest}
     * that contains a signature of a {@link de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim}
     * instances.</ul>
     * </li>
     *
     * @param requiredClaimResult the map of claimID to user base64 approvals of required claims.
     *                            Value not present (null) or empty would indicate a rejection of this claim.
     * @param optionalClaimResult the result that the permission contract resulted in.
     *                            Value not present (null) or empty would indicate a rejection of this claim.
     */
    void callback(Map<String, String> requiredClaimResult, Map<String, String> optionalClaimResult);
}
