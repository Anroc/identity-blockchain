package de.iosl.blockchain.identity.discovery.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

	private String ethID;
	private String publicKey;
	private String ip;
	private int port;

}
