package de.iosl.blockchain.identity.eba;

import de.iosl.blockchain.identity.eba.main.Account;

import java.io.File;
import java.nio.file.Path;

public interface EBAInterface {

    Account createWallet(String password, Path path);
    Account accessWallet(String pw, File file);

}
