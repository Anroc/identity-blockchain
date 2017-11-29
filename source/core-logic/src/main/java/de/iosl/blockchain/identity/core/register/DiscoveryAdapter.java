package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntry;
import feign.*;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface DiscoveryAdapter {

	String PROVIDER_PATH = "/provider";

	@RequestLine("GET " + PROVIDER_PATH + "?domainName={domainName}")
	List<RegistryEntry> getEntries(@Param("domainName") String domainName);

	@Bean
	static DiscoveryAdapter connect() {
		return Feign.builder()
				.target(DiscoveryAdapter.class, "srv01.snet.tu-berlin.de:8080");
	}
}
