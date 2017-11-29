package de.iosl.blockchain.identity.discovery.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryEntry {

	@Valid
	@NotNull
	private Payload payload;

	@Valid
	@NotNull
	private ECSignature signature;
	;
}
