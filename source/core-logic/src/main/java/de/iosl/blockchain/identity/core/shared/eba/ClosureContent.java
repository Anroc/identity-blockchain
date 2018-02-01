package de.iosl.blockchain.identity.core.shared.eba;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;
import java.util.Set;

import static de.iosl.blockchain.identity.core.shared.eba.PermissionContractContent.initMapKeys;

/**
 * Object representing a closure content request.
 *
 * Note: Hibernate annotations are not evaluated jet.
 */
@Data
public class ClosureContent {

    /**
     * Holds a unique set (keys) of encrypted (AES encrypted with the {@link #encryptedKey}) and encoded (base64) closure request
     * for the recipient of this smart contract.
     *
     * The value is an signed (base64) encoded request that the provider then can use to
     * retrieve the closure value.
     *
     * If this object gets instantiated this value shell not be empty.
     */
    @NotEmpty
    private final Map<String, String> encryptedRequests;
    @NotBlank
    private final String encryptedKey;

    public ClosureContent(@NonNull Set<String> encryptedRequests, @NonNull String encryptedKey) {
        this.encryptedRequests = initMapKeys(encryptedRequests, String.class);
        this.encryptedKey = encryptedKey;
    }
}
