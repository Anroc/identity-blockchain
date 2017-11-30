package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiscoveryClient {

	@Autowired
	private DiscoveryClientAdapter discoveryClientAdapter;

	public List<Payload> getEntries(String domainName) {
		Map<String, String> queryParam = new HashMap<>();

		if(domainName != null && ! domainName.isEmpty()) {
			queryParam.put("domainName", domainName);
		}

		return discoveryClientAdapter.getEntries(queryParam)
				.stream()
				.map(RegistryEntry::getPayload)
				.collect(Collectors.toList());
	}
}
