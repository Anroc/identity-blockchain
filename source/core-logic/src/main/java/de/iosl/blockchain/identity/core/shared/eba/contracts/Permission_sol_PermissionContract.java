package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b604051610465380380610465833981016040528080519190602001805160008054600160a060020a03338116600160a060020a031992831617835560018054918816919092161790556003805460ff191690559201919050805b82518260ff1610156100e257600280546001810161008783826100eb565b916000526020600020906002020160006040805190810160405280878760ff16815181106100b157fe5b9060200190602002015181526020018590529190508151815560208201516001918201559390930192506100699050565b50505050610143565b81548183558181151161011757600202816002028360005260206000209182019101610117919061011c565b505050565b61014091905b8082111561013c5760008082556001820155600201610122565b5090565b90565b610313806101526000396000f3006060604052600436106100615763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166372baf6178114610066578063c52822f8146100cc578063ec4a62ce146100df578063ed736d1d14610106575b600080fd5b341561007157600080fd5b610079610119565b60405160208082528190810183818151815260200191508051906020019060200280838360005b838110156100b85780820151838201526020016100a0565b505050509050019250505060405180910390f35b34156100d757600080fd5b6100796101e0565b34156100ea57600080fd5b6100f2610249565b604051901515815260200160405180910390f35b341561011157600080fd5b6100f2610285565b61012161028e565b600080805b60025460ff83161015610187576002805460ff841690811061014457fe5b9060005260206000209060020201905082805480600101828161016791906102a0565b506000918252602090912060018381015491909201559190910190610126565b828054806020026020016040519081016040528092919081815260200182805480156101d357602002820191906000526020600020905b815481526001909101906020018083116101be575b5050505050935050505090565b6101e861028e565b600080805b60025460ff83161015610187576002805460ff841690811061020b57fe5b9060005260206000209060020201905082805480600101828161022e91906102a0565b506000918252602090912082549101556001909101906101ed565b6001546000903373ffffffffffffffffffffffffffffffffffffffff90811691161461027457610282565b6003805460ff191660011790555b90565b60035460ff1690565b60206040519081016040526000815290565b8154818355818115116102c4576000838152602090206102c49181019083016102c9565b505050565b61028291905b808211156102e357600081556001016102cf565b50905600a165627a7a723058200c83a92441815faee1b63832cb840c36507dd6918c757a695e2d57aef1eff7730029";

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Permission_sol_PermissionContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<?> getSignedClaims() {
        Function function = new Function("getSignedClaims", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<?> getClaims() {
        Function function = new Function("getClaims", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<TransactionReceipt> setClaimsApproved() {
        Function function = new Function(
                "setClaimsApproved", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> getClaimsApproved() {
        Function function = new Function("getClaimsApproved", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, List<byte[]> _requriedClaims) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(_requriedClaims, org.web3j.abi.datatypes.generated.Bytes32.class))));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Permission_sol_PermissionContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, List<byte[]> _requriedClaims) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(_requriedClaims, org.web3j.abi.datatypes.generated.Bytes32.class))));
        return deployRemoteCall(Permission_sol_PermissionContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Permission_sol_PermissionContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Permission_sol_PermissionContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
