package de.iosl.blockchain.identity.discovery.registry.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

	private String ethID;
	private String publicKey;
	private String domainName;
	private int port;

}
