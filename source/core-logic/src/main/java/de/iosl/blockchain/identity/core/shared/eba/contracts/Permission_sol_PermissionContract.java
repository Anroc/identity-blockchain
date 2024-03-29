package de.iosl.blockchain.identity.core.shared.eba.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class Permission_sol_PermissionContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60405161049e38038061049e833981016040528080519190602001805182019190602001805160008054600160a060020a03338116600160a060020a031992831617909255600180549288169290911691909117905591506003905082805161007c9291602001906100a5565b5060028054600160a060020a031916600160a060020a0392909216919091179055506101409050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100e657805160ff1916838001178555610113565b82800160010185558215610113579182015b828111156101135782518255916020019190600101906100f8565b5061011f929150610123565b5090565b61013d91905b8082111561011f5760008155600101610129565b90565b61034f8061014f6000396000f3006060604052600436106100565763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166318004952811461005b57806325b6486d14610097578063c52822f8146100ea575b600080fd5b341561006657600080fd5b61006e610174565b60405173ffffffffffffffffffffffffffffffffffffffff909116815260200160405180910390f35b34156100a257600080fd5b6100e860046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061019195505050505050565b005b34156100f557600080fd5b6100fd6101d1565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610139578082015183820152602001610121565b50505050905090810190601f1680156101665780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60025473ffffffffffffffffffffffffffffffffffffffff165b90565b6001543373ffffffffffffffffffffffffffffffffffffffff9081169116146101b9576101ce565b60038180516101cc929160200190610279565b505b50565b6101d96102f7565b60038054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561026f5780601f106102445761010080835404028352916020019161026f565b820191906000526020600020905b81548152906001019060200180831161025257829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102ba57805160ff19168380011785556102e7565b828001600101855582156102e7579182015b828111156102e75782518255916020019190600101906102cc565b506102f3929150610309565b5090565b60206040519081016040526000815290565b61018e91905b808211156102f3576000815560010161030f5600a165627a7a72305820aa90bbff3c1104936773ed2e2d444425e63aa25e34f026985ecd6d01142efabe0029";

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> getClouserContractAddress() {
        Function function = new Function("getClouserContractAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setAndApproveClaims(String _claims) {
        Function function = new Function(
                "setAndApproveClaims", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_claims)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getClaims() {
        Function function = new Function("getClaims", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, String _claims, String _clouserContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Utf8String(_claims), 
                new org.web3j.abi.datatypes.Address(_clouserContract)));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, String _claims, String _clouserContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Utf8String(_claims), 
                new org.web3j.abi.datatypes.Address(_clouserContract)));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
