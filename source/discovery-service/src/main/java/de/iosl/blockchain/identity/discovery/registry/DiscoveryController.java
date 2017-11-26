package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import de.iosl.blockchain.identity.discovery.registry.validator.RegistryEntryValidator;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping("/provider")
public class DiscoveryController {

	@Autowired
	private DiscoveryService discoveryService;
	@Autowired
	private RegistryEntryValidator validator;

	@GetMapping("/{ethID}")
	public RegistryEntry getEntry(@PathVariable("ethID") String ethID) {
		return discoveryService.getEntry(ethID).orElseThrow(
				() -> new ServiceException("Could not find ethID [%s].", HttpStatus.NOT_FOUND, ethID)
		);
	}

	@PostMapping()
	public ResponseEntity<URL> createEntry(@Valid @RequestBody RegistryEntry registryEntry, HttpServletRequest request) {
		if( ! validator.isValid(registryEntry)) {
			throw new ServiceException("Permission denied.", HttpStatus.FORBIDDEN);
		}

		String baseUrl = String.format("%s://%s:%d/tasks/",request.getScheme(),  request.getServerName(), request.getServerPort());
		discoveryService.putEntry(registryEntry);
		return ResponseEntity.created(URI.create(baseUrl + registryEntry.getPayload().getEthID())).build();
	}
}
