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
public final class Permission_sol_PermissionContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051610464380380610464833981016040528080519190602001805160008054600160a060020a03338116600160a060020a031992831617909255600180549287169290911691909117905590910190506002818051610074929160200190610086565b50506003805460ff1916905550610121565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100c757805160ff19168380011785556100f4565b828001600101855582156100f4579182015b828111156100f45782518255916020019190600101906100d9565b50610100929150610104565b5090565b61011e91905b80821115610100576000815560010161010a565b90565b610334806101306000396000f3006060604052600436106100565763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166325b6486d811461005b578063c52822f8146100ae578063ed736d1d14610138575b600080fd5b341561006657600080fd5b6100ac60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061015f95505050505050565b005b34156100b957600080fd5b6100c16101ac565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100fd5780820151838201526020016100e5565b50505050905090810190601f16801561012a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561014357600080fd5b61014b610255565b604051901515815260200160405180910390f35b6001543373ffffffffffffffffffffffffffffffffffffffff908116911614610187576101a9565b600281805161019a92916020019061025e565b506003805460ff191660011790555b50565b6101b46102dc565b60028054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561024a5780601f1061021f5761010080835404028352916020019161024a565b820191906000526020600020905b81548152906001019060200180831161022d57829003601f168201915b505050505090505b90565b60035460ff1690565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061029f57805160ff19168380011785556102cc565b828001600101855582156102cc579182015b828111156102cc5782518255916020019190600101906102b1565b506102d89291506102ee565b5090565b60206040519081016040526000815290565b61025291905b808211156102d857600081556001016102f45600a165627a7a72305820148a2c7c6ddcee64bd8ed6a7179d8fc35faccebdfff554859d8386cae55a7d120029";

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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

    public RemoteCall<Boolean> getClaimsApproved() {
        Function function = new Function("getClaimsApproved", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, String _claims) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Utf8String(_claims)));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, String _claims) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Utf8String(_claims)));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
