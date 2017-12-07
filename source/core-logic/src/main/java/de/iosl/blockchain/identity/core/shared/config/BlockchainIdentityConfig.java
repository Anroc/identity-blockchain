package de.iosl.blockchain.identity.core.shared.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "blockchain.identity")
public class BlockchainIdentityConfig {

    @NotBlank
    private String protocol;

    @Valid
    private ServiceConfig core;

    @Valid
    private ServiceConfig discoveryService;
}
