package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class Registrar_sol_FirstContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60008054600160a060020a033316600160a060020a0319918216179091556001805460a060020a60ff02199216739a06cfa07f6d65d113ac9fcd1326355ae6db10831791909116905561014d806100676000396000f30060606040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633e8c060e8114610050578063f6c3132b14610077575b600080fd5b341561005b57600080fd5b61006361008f565b604051901515815260200160405180910390f35b341561008257600080fd5b61006360043515156100b0565b60015474010000000000000000000000000000000000000000900460ff1690565b6001546000903373ffffffffffffffffffffffffffffffffffffffff9081169116146100db57600080fd5b506001805474ff00000000000000000000000000000000000000001916740100000000000000000000000000000000000000009215158302179081905560ff91900416905600a165627a7a72305820bf58f352f9c1ffeeaf6478b405979166e7db60ea3276dd2b373d26a1158e22240029";

    private Registrar_sol_FirstContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Registrar_sol_FirstContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Boolean> getApproval() {
        Function function = new Function("getApproval", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setApproval(Boolean _approval) {
        Function function = new Function(
                "setApproval", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(_approval)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Registrar_sol_FirstContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Registrar_sol_FirstContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Registrar_sol_FirstContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Registrar_sol_FirstContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Registrar_sol_FirstContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Registrar_sol_FirstContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Registrar_sol_FirstContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Registrar_sol_FirstContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
