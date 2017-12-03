package de.iosl.blockchain.identity.core.register.keychain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyChain {

	private String privateKey;
	private String publicKey;

	private String algorithm;
}
