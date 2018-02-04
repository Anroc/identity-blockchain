package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b604051610412380380610412833981016040528080519190602001805160008054600160a060020a03338116600160a060020a03199283161790925560018054928716929091169190911790559091019050600281805161007492916020019061007c565b505050610117565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100bd57805160ff19168380011785556100ea565b828001600101855582156100ea579182015b828111156100ea5782518255916020019190600101906100cf565b506100f69291506100fa565b5090565b61011491905b808211156100f65760008155600101610100565b90565b6102ec806101266000396000f30060606040526004361061004b5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166325b6486d8114610050578063c52822f8146100a3575b600080fd5b341561005b57600080fd5b6100a160046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061012d95505050505050565b005b34156100ae57600080fd5b6100b661016d565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100f25780820151838201526020016100da565b50505050905090810190601f16801561011f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6001543373ffffffffffffffffffffffffffffffffffffffff9081169116146101555761016a565b6002818051610168929160200190610216565b505b50565b610175610294565b60028054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561020b5780601f106101e05761010080835404028352916020019161020b565b820191906000526020600020905b8154815290600101906020018083116101ee57829003601f168201915b505050505090505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061025757805160ff1916838001178555610284565b82800160010185558215610284579182015b82811115610284578251825591602001919060010190610269565b506102909291506102a6565b5090565b60206040519081016040526000815290565b61021391905b8082111561029057600081556001016102ac5600a165627a7a723058208877aa9d93a0f6a7a5fe3c119f6c0f393c420e87a9c71c04aae2c44ec8fb66e70029";

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
