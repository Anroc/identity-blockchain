package de.iosl.blockchain.identity.core.register.registry;

import de.iosl.blockchain.identity.core.config.BlockchainIdentityConfig;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryClientAdapterFactory {

	@Autowired
	private BlockchainIdentityConfig blockchainIdentityConfig;

	@Bean
	public DiscoveryClientAdapter connect() {
		String url = String.format("%s://%s:%d",
				blockchainIdentityConfig.getProtocol(),
				blockchainIdentityConfig.getDiscoveryService().getAddress(),
				blockchainIdentityConfig.getDiscoveryService().getPort());

		return Feign.builder()
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(DiscoveryClientAdapter.class, url);
	}
}
