package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b60405160408061078d833981016040528080519190602001805160008054600160a060020a03338116600160a060020a031992831617909255600180549690921695169490941790935550506003805460ff19169055600455610716806100776000396000f3006060604052600436106100775763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631375cdc1811461007c5780633008f93b146100cf57806341c0e1b51461015c5780634fa926a81461016f578063523aa273146101c05780638e78ef32146101e7575b600080fd5b341561008757600080fd5b6100cd60046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506101fd95505050505050565b005b34156100da57600080fd5b6100e5600435610259565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610121578082015183820152602001610109565b50505050905090810190601f16801561014e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561016757600080fd5b6100cd61031a565b341561017a57600080fd5b6100cd60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061035f95505050505050565b34156101cb57600080fd5b6101d3610387565b604051901515815260200160405180910390f35b34156101f257600080fd5b6100e5600435610391565b6001543373ffffffffffffffffffffffffffffffffffffffff90811691161461022557610256565b60028054600181016102378382610540565b6000928352602090922001828051610253929160200190610564565b50505b50565b6102616105e2565b600280548390811061026f57fe5b90600052602060002090018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561030e5780601f106102e35761010080835404028352916020019161030e565b820191906000526020600020905b8154815290600101906020018083116102f157829003601f168201915b50505050509050919050565b6000543373ffffffffffffffffffffffffffffffffffffffff9081169116146103425761035d565b60005473ffffffffffffffffffffffffffffffffffffffff16ff5b565b6000543373ffffffffffffffffffffffffffffffffffffffff90811691161461022557610256565b60035460ff165b90565b6103996105e2565b60015460009081903373ffffffffffffffffffffffffffffffffffffffff9081169116146103c657610539565b60028054859081106103d457fe5b600091825260209091206002549101925084106103f057610539565b50825b6002546000190181101561045f57600280546001830190811061041257fe5b906000526020600020900160028281548110151561042c57fe5b906000526020600020900190805460018160011615610100020316600290046104569291906105f4565b506001016103f3565b60028054600019810190811061047157fe5b906000526020600020900160006104889190610669565b600280549061049b906000198301610540565b50818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105315780601f1061050657610100808354040283529160200191610531565b820191906000526020600020905b81548152906001019060200180831161051457829003601f168201915b505050505092505b5050919050565b815481835581811511610253576000838152602090206102539181019083016106ad565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105a557805160ff19168380011785556105d2565b828001600101855582156105d2579182015b828111156105d25782518255916020019190600101906105b7565b506105de9291506106d0565b5090565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061062d57805485556105d2565b828001600101855582156105d257600052602060002091601f016020900482015b828111156105d257825482559160010191906001019061064e565b50805460018160011615610100020316600290046000825580601f1061068f5750610256565b601f01602090049060005260206000209081019061025691906106d0565b61038e91905b808211156105de5760006106c78282610669565b506001016106b3565b61038e91905b808211156105de57600081556001016106d65600a165627a7a723058201dd45618c56f79ce475fd1193f6ce58c7b660d9795dd1d7c8ca5ebfc16312b730029";

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

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, BigInteger _amountClousers) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountClousers)));
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, BigInteger _amountClousers) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountClousers)));
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
