package de.iosl.blockchain.identity.crypt.symmetic;

import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.symmetric.JsonSymmetricCryptEngine;

public class PasswordBasedCryptEngineTest extends JsonCryptEngineTest {

    private static final String PASSWD = "passwd";

    @Override
    public JsonSymmetricCryptEngine initCryptEngine() {
        return CryptEngine.generate()
                .with(128)
                .pbe(PASSWD);
    }

}
