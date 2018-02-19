package de.iosl.blockchain.identity.core.shared.api.permission;

import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequestPayload;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.eba.ClosureContent;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.crypt.asymmetic.AsymmetricCryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.ObjectSymmetricCryptEngine;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ClosureContentCryptEngine {

    private final AsymmetricCryptEngine<String> cryptEngine;
    private final KeyConverter keyConverter;

    public ClosureContentCryptEngine() {
        this.cryptEngine = CryptEngine.instance().string().rsa();
        this.keyConverter = new KeyConverter();
    }

    /**
     * Will encrypt the {@link ClosureContractRequestPayload#getStaticValue()} of the given closureContractRequests
     * with a symmetric generated secret that will then be encrypted with the given base64 public key.
     *
     * <b>Note:</b>
     * Will method has a side effect on {@link ClosureContractRequestPayload#getStaticValue()} and will replace
     * the value with the representative encrypted base64 string of this value. Keep this in mind while post processing.
     *
     * @param publicKey the base64 encoded public key
     * @param closureContractRequests the closure contract requests that shell be processed
     * @return the generated (partially encrypted) closure content object
     */
    public ClosureContent encrypt(@NonNull String publicKey, @NonNull Set<ClosureContractRequest> closureContractRequests) {
        if(closureContractRequests.isEmpty()) {
            return null;
        }
        log.info("Init new symmetric crypt engine for closure request and public key {}.", publicKey);
        ObjectSymmetricCryptEngine objectSymmetricCryptEngine = new ObjectSymmetricCryptEngine();
        log.info("Generating new shared secret.");
        Key symmetricKey = objectSymmetricCryptEngine.getSymmetricCipherKey();
        String base64Key = keyConverter.from(symmetricKey).toBase64();
        try {
            // encrypt key with users public key
            base64Key = cryptEngine.encrypt(base64Key, keyConverter.from(publicKey).toPublicKey());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidKeyException e) {
            throw new ServiceException("Users public key is malformed.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Generated encrypted shared secret: {}", base64Key);

        Set<String> closures = closureContractRequests
                .stream()
                .map(ccr -> {
                    try {
                        log.info("Encrypting {}", ccr);
                        return doEncrypt(ccr, symmetricKey, objectSymmetricCryptEngine);
                    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                        throw new ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .collect(Collectors.toSet());

        return new ClosureContent(closures, base64Key);
    }

    /**
     * Will decrypt the given (partially) encrypted {@link ClosureContent}. Specifically the value stored in
     * {@link ClosureContractRequestPayload#getStaticValue()}. To do so the encrypted shared secret will be
     * decrypted with the given private key and use this shared secret again to decrypt the value in
     * {@link ClosureContractRequestPayload#getStaticValue()}.
     *
     * <b>Note:</b>
     * The ClosureContentRequest you will get in return is already post processed. The raw object is base64 encoded
     * but the value in {@link ClosureContractRequestPayload#getStaticValue()} is encrypted. This method will automatically
     * replace the encrypted value with its decrypted representation.
     *
     * @param closureContent the closure content as it is in the blockchain
     * @param privateKey the private key
     * @return the decrypted ClosureContractRequest list.
     */
    public List<ClosureContractRequest> decrypt(ClosureContent closureContent, @NonNull PrivateKey privateKey) {
        if(closureContent == null) {
            return new ArrayList<>();
        }

        ObjectSymmetricCryptEngine symmetricObjectCryptEngine = new ObjectSymmetricCryptEngine();
        AsymmetricCryptEngine<String> asymmetricStringCryptEngine = CryptEngine.instance().string().rsa();

        log.info("Found {} closure requests", closureContent.getEncryptedRequests().size());

        // setup
        Key sharedSecret;
        try {
            log.info("Extracting shared secret");
            String secretBase64 = asymmetricStringCryptEngine.decrypt(closureContent.getEncryptedKey(), privateKey);
            sharedSecret = keyConverter.from(secretBase64).toSymmetricKey();
        } catch (IllegalBlockSizeException  | BadPaddingException e) {
            throw new ServiceException("Could not decrypt shared secret.", e);
        } catch (InvalidKeyException e) {
            throw new ServiceException("Private key is malformed.", e);
        }

        // decrypt content
        return closureContent.getEncryptedRequests().stream()
                .map(encryptedRequest -> {
                    try {
                        log.info("Decrypting content...");
                        ClosureContractRequest closureContractRequest = doDecrypt(encryptedRequest, sharedSecret, symmetricObjectCryptEngine);
                        log.info("Decrypted: {}", closureContractRequest);
                        return closureContractRequest;
                    } catch (IllegalBlockSizeException  | BadPaddingException e) {
                        throw new ServiceException("Could not decrypt shared secret.", e);
                    } catch (InvalidKeyException e) {
                        throw new ServiceException("Private key is malformed.", e);
                    }
                })
                .collect(Collectors.toList());
    }

    private String doEncrypt(ClosureContractRequest ccr, Key symmetricKey, ObjectSymmetricCryptEngine objectSymmetricCryptEngine)
            throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {

        // encrypt value holder
        ValueHolder valueHolder = ccr.getClosureContractRequestPayload().getStaticValue();
        String encryptedValueHolder = objectSymmetricCryptEngine.encrypt(valueHolder, symmetricKey);
        ccr.getClosureContractRequestPayload().setStaticValue(new ValueHolder(encryptedValueHolder));

        // convert object to string
        return objectSymmetricCryptEngine.serializeToBase64String(ccr);
    }

    private ClosureContractRequest doDecrypt(String base64, Key symmetricKey, ObjectSymmetricCryptEngine objectSymmetricCryptEngine)
            throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        // convert to object
        ClosureContractRequest closureContractRequest = objectSymmetricCryptEngine.deserializeFromBase64String(base64, ClosureContractRequest.class);

        // decrypt value holder and restore old layout
        String encrypted = closureContractRequest.getClosureContractRequestPayload().getStaticValue().getUnifiedValueAs(String.class);
        ValueHolder valueHolder = objectSymmetricCryptEngine.decryptAndCast(encrypted, symmetricKey, ValueHolder.class);
        closureContractRequest.getClosureContractRequestPayload().setStaticValue(valueHolder);

        return closureContractRequest;
    }
}
