package de.iosl.blockchain.identity.eba;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
public class Account {

    private final String address;
    private final BigInteger privateKey;
    private final BigInteger publicKey;

}
