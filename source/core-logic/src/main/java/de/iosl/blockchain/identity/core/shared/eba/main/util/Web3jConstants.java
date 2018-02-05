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

	public static final BigInteger GAS_PRICE = BigInteger.ZERO;
	public static final BigInteger GAS_PRICE_2_ETHER = BigInteger.valueOf(20000000000L);
	
	public static final BigInteger GAS_LIMIT_ETHER_TX = BigInteger.valueOf(21_000);
	public static final BigInteger GAS_LIMIT_GREETER_TX = BigInteger.valueOf(500_000L);
	public static final BigInteger GAS_LIMIT_REGISTRAR_TX = BigInteger.valueOf(500_000L);
	public static final BigInteger GAS_LIMIT_PERMISSION_TX = BigInteger.valueOf(5_000_000L);

	public static final int CONFIRMATION_ATTEMPTS = 5;
	public static final int SLEEP_DURATION = 1000;

	// file name extensions for smart contracts
	public static final String EXT_SOLIDITY = "sol";
	public static final String EXT_BINARY = "bin";
	public static final String EXT_ABI = "abi";

	public static final String DEFAULT_START_AMOUNT = "0.0";
	public static final String GOV_MONEY_FROM_COAINBASE = "0.0";


	public static BigInteger amountToEther(String amount) {
		return Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
	}
}
