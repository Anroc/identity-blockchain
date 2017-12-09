package de.iosl.blockchain.identity.eba.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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



}
