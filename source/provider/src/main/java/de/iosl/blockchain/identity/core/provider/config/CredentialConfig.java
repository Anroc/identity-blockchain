package de.iosl.blockchain.identity.core.provider.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "blockchain.identity.provider.credentials")
public class CredentialConfig {

    @NotBlank
    private String username;
    @NotNull
    private String password;
}
