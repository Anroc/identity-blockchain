package de.iosl.blockchain.identity.core.shared.eba.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
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
public final class Closure_sol_ClosureContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60405161096f38038061096f8339810160405280805191906020018051919060200180519190602001805160008054600160a060020a03338116600160a060020a03199283161790925560018054928916929091169190911790556005805460ff191690556007849055909101905060088180516100919291602001906100bc565b505060028054600160a060020a031916600160a060020a039390931692909217909155506101579050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100fd57805160ff191683800117855561012a565b8280016001018555821561012a579182015b8281111561012a57825182559160200191906001019061010f565b5061013692915061013a565b5090565b61015491905b808211156101365760008155600101610140565b90565b610809806101666000396000f3006060604052600436106100da5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630d47416281146100df5780630f6a4071146101695780633fc7f8031461018e57806341c0e1b5146101a4578063523aa273146101b95780635b456425146101e0578063832880e7146101f6578063893d20e814610225578063a939f83014610238578063c47692d61461024b578063c4fb979e1461025e578063d57e4234146102af578063e16208fd146102c2578063ec3edff3146102d8578063f358c57614610329575b600080fd5b34156100ea57600080fd5b6100f261037a565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561012e578082015183820152602001610116565b50505050905090810190601f16801561015b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561017457600080fd5b61017c610423565b60405190815260200160405180910390f35b341561019957600080fd5b6100f2600435610429565b34156101af57600080fd5b6101b76104ea565b005b34156101c457600080fd5b6101cc610515565b604051901515815260200160405180910390f35b34156101eb57600080fd5b6100f260043561051e565b341561020157600080fd5b610209610534565b604051600160a060020a03909116815260200160405180910390f35b341561023057600080fd5b610209610543565b341561024357600080fd5b610209610552565b341561025657600080fd5b6100f2610561565b341561026957600080fd5b6101b760046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506105d495505050505050565b34156102ba57600080fd5b61017c610607565b34156102cd57600080fd5b6101b760043561060d565b34156102e357600080fd5b6101b760046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061062d95505050505050565b341561033457600080fd5b6101b760046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061067b95505050505050565b6103826106a8565b60098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104185780601f106103ed57610100808354040283529160200191610418565b820191906000526020600020905b8154815290600101906020018083116103fb57829003601f168201915b505050505090505b90565b60075490565b6104316106a8565b600480548390811061043f57fe5b90600052602060002090018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104de5780601f106104b3576101008083540402835291602001916104de565b820191906000526020600020905b8154815290600101906020018083116104c157829003601f168201915b50505050509050919050565b60005433600160a060020a0390811691161461050557610513565b600054600160a060020a0316ff5b565b60055460ff1690565b6105266106a8565b600380548390811061043f57fe5b600154600160a060020a031690565b600054600160a060020a031690565b600254600160a060020a031690565b6105696106a8565b60088054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104185780601f106103ed57610100808354040283529160200191610418565b60015433600160a060020a039081169116146105ef57610604565b60098180516106029291602001906106ba565b505b50565b60065490565b60015433600160a060020a0390811691161461062857610604565b600655565b60005433600160a060020a0390811691161461064857610604565b600380546001810161065a8382610738565b60009283526020909220018280516106769291602001906106ba565b505050565b60015433600160a060020a0390811691161461069657610604565b600480546001810161065a8382610738565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106fb57805160ff1916838001178555610728565b82800160010185558215610728579182015b8281111561072857825182559160200191906001019061070d565b5061073492915061075c565b5090565b81548183558181151161067657600083815260209020610676918101908301610776565b61042091905b808211156107345760008155600101610762565b61042091905b808211156107345760006107908282610799565b5060010161077c565b50805460018160011615610100020316600290046000825580601f106107bf5750610604565b601f016020900490600052602060002090810190610604919061075c5600a165627a7a72305820191ad8ed1b6a6ba70b2501638fd9b67f1ff5595fc7f37a6c8540927c4187b39f0029";

    private Closure_sol_ClosureContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Closure_sol_ClosureContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> getEncryptedKeyForApprovedClosure() {
        Function function = new Function("getEncryptedKeyForApprovedClosure", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getRequestedClosureAmount() {
        Function function = new Function("getRequestedClosureAmount", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getApprovedClosureByIndex(BigInteger index) {
        Function function = new Function("getApprovedClosureByIndex", 
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

    public RemoteCall<Boolean> getsetAllClaims() {
        Function function = new Function("getsetAllClaims", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getRequestedClosureByIndex(BigInteger index) {
        Function function = new Function("getRequestedClosureByIndex", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getUser() {
        Function function = new Function("getUser", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getOwner() {
        Function function = new Function("getOwner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getRequestingProvider() {
        Function function = new Function("getRequestingProvider", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getEncryptedKeyForRequestedClosure() {
        Function function = new Function("getEncryptedKeyForRequestedClosure", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setEncryptedKeyForApprovedClosure(String _encryptedKeyApprovedClosure) {
        Function function = new Function(
                "setEncryptedKeyForApprovedClosure", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_encryptedKeyApprovedClosure)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getApprovedClosureAmount() {
        Function function = new Function("getApprovedClosureAmount", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setApprovedClosureAmount(BigInteger amount) {
        Function function = new Function(
                "setApprovedClosureAmount", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addClosureAsRequestingProvider(String ClosureContent) {
        Function function = new Function(
                "addClosureAsRequestingProvider", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ClosureContent)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addToApprovedClosureAsUser(String ClosureContent) {
        Function function = new Function(
                "addToApprovedClosureAsUser", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ClosureContent)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Closure_sol_ClosureContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, String _requestingProvider, BigInteger _amountRequestedClosures, String _encryptedKeyRequestedClosure) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Address(_requestingProvider), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountRequestedClosures), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedKeyRequestedClosure)));
        return deployRemoteCall(Closure_sol_ClosureContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Closure_sol_ClosureContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, String _requestingProvider, BigInteger _amountRequestedClosures, String _encryptedKeyRequestedClosure) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Address(_requestingProvider), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountRequestedClosures), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedKeyRequestedClosure)));
        return deployRemoteCall(Closure_sol_ClosureContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Closure_sol_ClosureContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Closure_sol_ClosureContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Closure_sol_ClosureContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Closure_sol_ClosureContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
