package de.iosl.blockchain.identity.core.shared.api.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoDTO {

    private String buildVersion;
    private String apiVersion;
    private String applicationName;
}
