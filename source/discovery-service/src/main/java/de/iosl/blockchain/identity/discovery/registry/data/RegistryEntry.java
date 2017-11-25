package de.iosl.blockchain.identity.discovery.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryEntry {

	private Payload payload;
	private String mac;
}
