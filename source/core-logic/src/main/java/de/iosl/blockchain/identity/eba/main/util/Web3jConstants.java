package de.iosl.blockchain.identity.eba.main.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@Configuration("blockchain.identity.etherum")
public class Web3jConstants {

	private String address;
	private String port;

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
	
	public static final BigInteger GAS_LIMIT_ETHER_TX = BigInteger.valueOf(21_000);
	public static final BigInteger GAS_LIMIT_GREETER_TX = BigInteger.valueOf(500_000L);

	public static final int CONFIRMATION_ATTEMPTS = 40;
	public static final int SLEEP_DURATION = 1000;

	// file name extensions for smart contracts
	public static final String EXT_SOLIDITY = "sol";
	public static final String EXT_BINARY = "bin";
	public static final String EXT_ABI = "abi";
}
