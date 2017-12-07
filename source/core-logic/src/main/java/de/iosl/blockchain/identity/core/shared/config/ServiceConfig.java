package de.iosl.blockchain.identity.core.shared.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
public class ServiceConfig {

    @Min(1024)
    private int port;

    @NotBlank
    private String address;
}
