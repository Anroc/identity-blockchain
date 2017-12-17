package de.iosl.blockchain.identity.eba.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.math.BigInteger;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Account {

    private String address;
    private BigInteger publicKey;
    private BigInteger privateKey;
    private File file;

    public Credentials getCredentials(){
        return Credentials.create(getEcKeyPair());
    }

    public ECKeyPair getEcKeyPair(){
        return new ECKeyPair(this.privateKey, this.publicKey);
    }
}
