package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import de.iosl.blockchain.identity.discovery.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/provider")
public class DiscoveryController {

    @Autowired
    private DiscoveryService discoveryService;
    @Autowired
    private ECSignatureValidator validator;

    @GetMapping("/{ethID}")
    public RequestDTO<RegistryEntryDTO> getEntry(@PathVariable("ethID") String ethID) {
        return discoveryService.getEntry(ethID)
                .map(RegistryEntry::toDTO)
                .orElseThrow(
                () -> new ServiceException("Could not find ethID [%s].",
                        HttpStatus.NOT_FOUND, ethID)
        );
    }

    @GetMapping()
    public List<RequestDTO<RegistryEntryDTO>> getEntries(
            @RequestParam(value = "domainName", defaultValue = "") final String domainName) {
        return discoveryService.getEntries().stream().filter(registerEntry ->
                domainName.isEmpty() || registerEntry.getClientInformation()
                        .getDomainName().equals(domainName))
                .map(RegistryEntry::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping()
    public ResponseEntity<URL> createEntry(
            @Valid @RequestBody RequestDTO<RegistryEntryDTO> registryEntry,
            HttpServletRequest request) {
        if (!validator.isValid(registryEntry)) {
            throw new ServiceException("Permission denied.",
                    HttpStatus.FORBIDDEN);
        }

        String baseUrl = String.format("%s://%s:%d/tasks/", request.getScheme(),
                request.getServerName(), request.getServerPort());
        discoveryService.putEntry(new RegistryEntry(registryEntry.getPayload(), registryEntry.getSignature(), registryEntry.getPayload().getEthID()));
        return ResponseEntity.created(
                URI.create(baseUrl + registryEntry.getPayload().getEthID()))
                .build();
    }
}
