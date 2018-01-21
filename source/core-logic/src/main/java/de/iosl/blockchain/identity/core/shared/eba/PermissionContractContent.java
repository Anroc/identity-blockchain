package de.iosl.blockchain.identity.core.shared.eba;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
public class PermissionContractContent {

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
        this.requiredClaims = initMapKeys(requiredClaims, String.class);
        this.optionalClaims = initMapKeys(optionalClaims, String.class);

        this.requesterAddress = requesterAddress;
    }

    /**
     * Initializes a new map for the given key set.
     *
     * @param keys the keys that shell be present in the map.
     * @param valueType the type class of the values
     * @param <K> Key type
     * @param <V> Value type
     * @return initialized map
     */
    private final <K, V> Map<K, V> initMapKeys(@NonNull Set<K> keys, @SuppressWarnings("unused") Class<V> valueType) {
        Map<K, V> map = new HashMap<>();
        keys.stream().forEach(key -> map.put(key, null));
        return map;
    }

}
