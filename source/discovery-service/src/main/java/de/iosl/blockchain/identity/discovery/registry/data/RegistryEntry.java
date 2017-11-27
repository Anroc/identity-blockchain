package de.iosl.blockchain.identity.discovery.registry.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryEntry {

	@Valid
	@NotNull
	private Payload payload;

	@NotBlank
	private String mac;
}
