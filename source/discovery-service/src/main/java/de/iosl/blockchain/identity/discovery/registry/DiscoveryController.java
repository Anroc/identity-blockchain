package de.iosl.blockchain.identity.discovery.registry;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/provider")
public class DiscoveryController {

	@Autowired
	private DiscoveryService discoveryService;

	@GetMapping("/{ethID}")
	public RegistryEntry getEntry(@PathVariable("ethID") String ethID) {
		return null;
	}

}
