package de.iosl.blockchain.identity.eth.wallet;

import de.iosl.blockchain.identity.crypt.KeyConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.web3j.crypto.Credentials;

import java.security.PublicKey;

@Data
@RequiredArgsConstructor
public class CredentialsWrapper {

	private final Credentials credentials;

	public PublicKey getPublicKey() {
		return KeyConverter.from(credentials.getEcKeyPair().getPublicKey().toByteArray())
				.toPublicKey();
	}

}
