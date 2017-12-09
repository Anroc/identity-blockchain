package de.iosl.blockchain.identity.core.shared.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

    private String ethID;
    private String publicKey;
    private String domainName;
    private int port;
}
