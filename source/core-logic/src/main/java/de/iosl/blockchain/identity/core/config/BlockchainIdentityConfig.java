package de.iosl.blockchain.identity.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="blockchain.identity")
public class BlockchainIdentityConfig {

	@NotBlank
	private String address;

	@NotBlank
	private String protocol;

	@Valid
	private DiscoveryServiceConfig discoveryService;

	@Data
	public static class DiscoveryServiceConfig {

		@Min(1025)
		private int port;
	}
}
