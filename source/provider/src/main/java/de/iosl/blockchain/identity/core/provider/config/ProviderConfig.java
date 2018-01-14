package de.iosl.blockchain.identity.core.provider.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "blockchain.identity.provider")
public class ProviderConfig {

    @NotBlank private String buildVersion;
    @NotBlank private String apiVersion;
    @NotBlank private String stateWallet;

    @Value("${spring.couchbase.bucket.name}")
    @NotBlank private String applicationName;
}
