package de.iosl.blockchain.identity.core.shared.api.permission;

import de.iosl.blockchain.identity.core.shared.api.permission.data.ClosureContractRequest;
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
import java.util.HashSet;
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
                        return objectSymmetricCryptEngine.encrypt(ccr, symmetricKey);
                    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                        throw new ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .collect(Collectors.toSet());

        return new ClosureContent(closures, base64Key);
    }

    public Set<ClosureContractRequest> decrypt(ClosureContent closureContent, @NonNull PrivateKey privateKey) {
        if(closureContent == null) {
            return new HashSet<>();
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
                        ClosureContractRequest closureContractRequestDTO = symmetricObjectCryptEngine.decryptAndCast(
                                encryptedRequest,
                                sharedSecret,
                                ClosureContractRequest.class);
                        log.info("Decrypted: {}", closureContractRequestDTO);
                        return closureContractRequestDTO;
                    } catch (IllegalBlockSizeException  | BadPaddingException e) {
                        throw new ServiceException("Could not decrypt shared secret.", e);
                    } catch (InvalidKeyException e) {
                        throw new ServiceException("Private key is malformed.", e);
                    }
                })
                .collect(Collectors.toSet());
    }
}
