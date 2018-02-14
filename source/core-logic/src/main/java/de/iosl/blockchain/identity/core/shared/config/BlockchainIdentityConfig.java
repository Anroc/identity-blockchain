package de.iosl.blockchain.identity.core.shared.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iosl.blockchain.identity.core.shared.eba.main.util.Web3jConstants;
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

    @NotBlank private String protocol;
    @Valid private ServiceConfig core;
    @Valid private ServiceConfig discoveryService;

    @Valid
    private Web3jConstants ethereum;

    private ClientType type;

    private String name;

    @JsonIgnore
    public String getHostUrl() {
        return String.format(
                "%s://%s:%d", getProtocol(), getCore().getAddress(), getCore().getPort()
        );
    }
}
