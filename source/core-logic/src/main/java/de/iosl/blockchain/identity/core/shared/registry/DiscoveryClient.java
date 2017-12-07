package de.iosl.blockchain.identity.core.shared.registry;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.discovery.registry.data.ECSignature;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiscoveryClient {

    @Getter
    private final EthereumSigner signer;
    @Autowired
    private DiscoveryClientAdapter discoveryClientAdapter;
    @Autowired
    private BlockchainIdentityConfig config;

    public DiscoveryClient() {
        this.signer = new EthereumSigner();
    }

    /**
     * Requests all registered entries from the discovery service.
     * Verifies the signature of all. If there is a mismatch this entry will not be returned.
     *
     * @param domainName optional query parameter
     * @return list of all entries
     */
    public List<Payload> getEntries(String domainName) {
        Map<String, String> queryParam = new HashMap<>();

        if (domainName != null && !domainName.isEmpty()) {
            queryParam.put("domainName", domainName);
        }

        return discoveryClientAdapter.getEntries(queryParam)
                .stream()
                .filter(this::isSignatureValid)
                .map(RegistryEntryDTO::getPayload)
                .collect(Collectors.toList());
    }

    /**
     * Returns the Payload or throws a 404 Not found exception if the ethID was not referenced in
     * the discovery service. This method will also validate the signature and filter all objects
     * where the signature is invalid.
     *
     * @param ethID the ethId that shell be found
     * @return the found payload
     */
    public Payload getEntry(@NonNull String ethID) {
        return discoveryClientAdapter.getEntry(ethID)
                .filter(this::isSignatureValid)
                .map(RegistryEntryDTO::getPayload)
                .orElseThrow(
                        () -> new ServiceException(
                                "Could not find provider with ethID: [%s].",
                                HttpStatus.NOT_FOUND, ethID)
                );
    }

    /**
     * Registers a new entry into the discovery service.
     * Generates a signature.
     *
     * @param ethID        the ethId that shell be sued
     * @param rsaPublicKey the public key of the service
     * @param ecPrivateKey the ethereum key that shell be used for signing
     */
    public void register(@NonNull String ethID, @NonNull String rsaPublicKey,
            @NonNull BigInteger ecPrivateKey) {
        Payload payload = new Payload();
        payload.setEthID(ethID);
        payload.setDomainName(config.getCore().getAddress());
        payload.setPort(config.getCore().getPort());
        payload.setPublicKey(rsaPublicKey);

        ECSignature ecSignature = ECSignature.fromSignatureData(
                getSigner().sign(payload, ECKeyPair.create(ecPrivateKey))
        );

        discoveryClientAdapter.register(
                new RegistryEntryDTO(payload, ecSignature)
        );
    }

    protected boolean isSignatureValid(
            @NonNull RegistryEntryDTO registryEntry) {
        return getSigner().verifySignature(
                registryEntry.getPayload(),
                registryEntry.getSignature().toSignatureData(),
                registryEntry.getPayload().getEthID());
    }

}
