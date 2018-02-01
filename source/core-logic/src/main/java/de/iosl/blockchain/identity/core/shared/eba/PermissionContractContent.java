package de.iosl.blockchain.identity.core.shared.eba;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;
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
 * Note: Hibernate annotation are not jet evaluated
 * see https://stackoverflow.com/a/40887477/6190424 for information how to do so
 */
@Data
@RequiredArgsConstructor
public class PermissionContractContent implements Serializable{

    /**
     * The map of claimID to user base64 approvals of required claims.
     * Value not present (null) or empty would indicate a rejection of this claim.
     */
    @NotEmpty
    private final Map<String, String> requiredClaims;

    /**
     * The result that the permission contract resulted in.
     * Value not present (null) or empty would indicate a rejection of this claim.
     */
    private final Map<String, String> optionalClaims;

    /**
     * The address of the third party requesting the claims.
     */
    @NotBlank
    private final String requesterAddress;

    /**
     * Content for eventual closure request.
     *
     * Null of not present.
     */
    @Valid
    private final ClosureContent closureContent;

    public PermissionContractContent(@NonNull Set<String> requiredClaims, @NonNull Set<String> optionalClaims, @NonNull String requesterAddress) {
        this(requiredClaims, optionalClaims, requesterAddress, null);
    }

    public PermissionContractContent(@NonNull Set<String> requiredClaims, @NonNull Set<String> optionalClaims, @NonNull String requesterAddress, ClosureContent closureContent) {
        this.requiredClaims = initMapKeys(requiredClaims, String.class);
        this.optionalClaims = initMapKeys(optionalClaims, String.class);

        this.requesterAddress = requesterAddress;

        this.closureContent = closureContent;
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
    protected static final <K, V> Map<K, V> initMapKeys(@NonNull Set<K> keys, @SuppressWarnings("unused") Class<V> valueType) {
        Map<K, V> map = new HashMap<>();
        keys.stream().forEach(key -> map.put(key, null));
        return map;
    }

}
