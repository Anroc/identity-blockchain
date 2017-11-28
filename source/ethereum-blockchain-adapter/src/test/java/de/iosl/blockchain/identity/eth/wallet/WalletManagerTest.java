package de.iosl.blockchain.identity.eth.wallet;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;

public class WalletManagerTest {

	private static final String TEST_PW = "asd";
	private static final String FILE = "sample_wallet.json";

	private File file;

	@Before
	public void setup() throws IOException {
		ClassPathResource resource = new ClassPathResource(FILE);
		file = resource.getFile();
	}

	@Test
	public void loadWalletTest() throws Exception {
		WalletManager walletManager = new WalletManager();
		CredentialsWrapper credentialsWrapper = walletManager.loadWallet(TEST_PW, file);
		credentialsWrapper.getPublicKey();
	}

	@Test public void asd() {
		ECKeyPair.create(CryptEngine.generate().string().rsa().getAsymmetricCipherKeyPair());
	}
}
