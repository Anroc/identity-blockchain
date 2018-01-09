package de.iosl.blockchain.identity.core.shared.eba.main.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.web3j.utils.Convert;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class Web3jConstants {

	@NotBlank
	private String address;
	@NotBlank
	private String port;

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
	
	public static final BigInteger GAS_LIMIT_ETHER_TX = BigInteger.valueOf(21_000);
	public static final BigInteger GAS_LIMIT_GREETER_TX = BigInteger.valueOf(500_000L);
	public static final BigInteger GAS_LIMIT_REGISTRAR_TX = BigInteger.valueOf(500_000L);

	public static final int CONFIRMATION_ATTEMPTS = 40;
	public static final int SLEEP_DURATION = 1000;

	// file name extensions for smart contracts
	public static final String EXT_SOLIDITY = "sol";
	public static final String EXT_BINARY = "bin";
	public static final String EXT_ABI = "abi";

	public static final String DEFAULT_START_AMOUNT = "2.0";
	public static final BigInteger DEFAULT_START_ETHER = Convert.toWei(Web3jConstants.DEFAULT_START_AMOUNT, Convert.Unit.ETHER).toBigInteger();
}
