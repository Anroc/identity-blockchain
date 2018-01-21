package de.iosl.blockchain.identity.core.shared.eba;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the result of the given permission contract.
 *
 * This maps consist of:
 * <li>
 * <ul>Key is the claimID.</ul>
 * <ul>Value is the base64 encoded {@link de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest}
 * that contains a signature of a {@link de.iosl.blockchain.identity.core.shared.api.permission.data.dto.ApprovedClaim}
 * instances.</ul>
 * </li>
 *
 * All fields are strict to be not null.
 */
@Data
@RequiredArgsConstructor
public class PermissionContractContent implements Serializable{

    /**
     * the map of claimID to user base64 approvals of required claims.
     * Value not present (null) or empty would indicate a rejection of this claim.
     */
    private final Map<String, String> requiredClaims;

    /**
     * the result that the permission contract resulted in.
     * Value not present (null) or empty would indicate a rejection of this claim.
     */
    private final Map<String, String> optionalClaims;

    /**
     * The address of the third party requesting the claims.
     */
    private final String requesterAddress;

    public PermissionContractContent(@NonNull Set<String> requiredClaims, @NonNull Set<String> optionalClaims, @NonNull String requesterAddress) {
        this.requiredClaims = requiredClaims.stream().collect(Collectors.toMap(s -> s, null));
        this.optionalClaims = optionalClaims.stream().collect(Collectors.toMap(s -> s, null));
        this.requesterAddress = requesterAddress;
    }

}
