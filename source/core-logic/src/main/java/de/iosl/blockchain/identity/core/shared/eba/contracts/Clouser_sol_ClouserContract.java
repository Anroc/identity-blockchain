package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public final class Clouser_sol_ClouserContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b61074e8061001e6000396000f3006060604052600436106100825763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631375cdc181146100875780633008f93b146100da57806341c0e1b5146101675780634fa926a81461017a578063523aa273146101cb5780638e78ef32146101f2578063bd78c96e14610208575b600080fd5b341561009257600080fd5b6100d860046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061022a95505050505050565b005b34156100e557600080fd5b6100f0600435610279565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561012c578082015183820152602001610114565b50505050905090810190601f1680156101595780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561017257600080fd5b6100d861033a565b341561018557600080fd5b6100d860046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061036595505050505050565b34156101d657600080fd5b6101de610380565b604051901515815260200160405180910390f35b34156101fd57600080fd5b6100f060043561038a565b341561021357600080fd5b6100d8600160a060020a036004351660243561052c565b60015433600160a060020a0390811691161461024557610276565b60028054600181016102578382610578565b600092835260209092200182805161027392916020019061059c565b50505b50565b61028161061a565b600280548390811061028f57fe5b90600052602060002090018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561032e5780601f106103035761010080835404028352916020019161032e565b820191906000526020600020905b81548152906001019060200180831161031157829003601f168201915b50505050509050919050565b60005433600160a060020a0390811691161461035557610363565b600054600160a060020a0316ff5b565b60005433600160a060020a0390811691161461024557610276565b60035460ff165b90565b61039261061a565b600154600090819033600160a060020a039081169116146103b257610525565b60028054859081106103c057fe5b600091825260209091206002549101925084106103dc57610525565b50825b6002546000190181101561044b5760028054600183019081106103fe57fe5b906000526020600020900160028281548110151561041857fe5b9060005260206000209001908054600181600116156101000203166002900461044292919061062c565b506001016103df565b60028054600019810190811061045d57fe5b9060005260206000209001600061047491906106a1565b6002805490610487906000198301610578565b50818054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561051d5780601f106104f25761010080835404028352916020019161051d565b820191906000526020600020905b81548152906001019060200180831161050057829003601f168201915b505050505092505b5050919050565b60008054600160a060020a0333811673ffffffffffffffffffffffffffffffffffffffff199283161790925560018054949092169316929092179091556003805460ff19169055600455565b815481835581811511610273576000838152602090206102739181019083016106e5565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105dd57805160ff191683800117855561060a565b8280016001018555821561060a579182015b8281111561060a5782518255916020019190600101906105ef565b50610616929150610708565b5090565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610665578054855561060a565b8280016001018555821561060a57600052602060002091601f016020900482015b8281111561060a578254825591600101919060010190610686565b50805460018160011615610100020316600290046000825580601f106106c75750610276565b601f0160209004906000526020600020908101906102769190610708565b61038791905b808211156106165760006106ff82826106a1565b506001016106eb565b61038791905b80821115610616576000815560010161070e5600a165627a7a72305820404324808c664a771d77ab28f4930e5d14fbf892b7977274273c4075dbe670b60029";

    private Clouser_sol_ClouserContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Clouser_sol_ClouserContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> addClouserAsUser(String clouserContent) {
        Function function = new Function(
                "addClouserAsUser", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(clouserContent)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getClouserById(BigInteger index) {
        Function function = new Function("getClouserById", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addClouserAsRequestingProvider(String clouserContent) {
        Function function = new Function(
                "addClouserAsRequestingProvider", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(clouserContent)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> getsetAllClaims() {
        Function function = new Function("getsetAllClaims", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> getRequestedClouserAsUserAndDeleteIndex(BigInteger index) {
        Function function = new Function(
                "getRequestedClouserAsUserAndDeleteIndex", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> PermissionContract(String _user, BigInteger _amountClousers) {
        Function function = new Function(
                "PermissionContract", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountClousers)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
