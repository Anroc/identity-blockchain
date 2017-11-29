package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscoveryClient {

	@Autowired
	private DiscoveryAdapter discoveryAdapter;

	public List<Payload> getEntries() {
		return discoveryAdapter.getEntries("")
				.stream()
				.map(RegistryEntry::getPayload)
				.collect(Collectors.toList());
	}
}
