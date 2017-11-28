package de.iosl.blockchain.identity.eth.wallet;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

@Component
public class WalletManager {

	public static final String ETHEREUM_HOME_DIR = "~/.ethereum";

	public CredentialsWrapper loadWallet(@NonNull String password, @NonNull File file) throws IOException, CipherException {
		Credentials credentials = WalletUtils.loadCredentials(password, file);
		return new CredentialsWrapper(credentials);
	}

}
