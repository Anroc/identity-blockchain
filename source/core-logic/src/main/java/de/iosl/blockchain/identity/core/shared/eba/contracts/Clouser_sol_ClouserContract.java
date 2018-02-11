package de.iosl.blockchain.identity.core.shared.eba.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516108503803806108508339810160405280805191906020018051919060200180519190602001805160008054600160a060020a03338116600160a060020a03199283161790925560018054928916929091169190911790556005805460ff191690556007849055909101905060088180516100919291602001906100bc565b505060028054600160a060020a031916600160a060020a039390931692909217909155506101579050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100fd57805160ff191683800117855561012a565b8280016001018555821561012a579182015b8281111561012a57825182559160200191906001019061010f565b5061013692915061013a565b5090565b61015491905b808211156101365760008155600101610140565b90565b6106ea806101666000396000f3006060604052600436106100c45763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631fb4ca2d81146100c957806341c0e1b51461011c5780634ce452c41461012f5780634fa926a814610145578063523aa2731461019657806367012eae146101bd5780636f8f13911461024a57806375b371181461026f578063832880e714610282578063893d20e8146102b1578063a1bc5a83146102c4578063a939f830146102d7578063cb9e7176146102ea575b600080fd5b34156100d457600080fd5b61011a60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061030095505050505050565b005b341561012757600080fd5b61011a61034f565b341561013a57600080fd5b61011a60043561037a565b341561015057600080fd5b61011a60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061039a95505050505050565b34156101a157600080fd5b6101a96103c7565b604051901515815260200160405180910390f35b34156101c857600080fd5b6101d36004356103d1565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561020f5780820151838201526020016101f7565b50505050905090810190601f16801561023c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561025557600080fd5b61025d610492565b60405190815260200160405180910390f35b341561027a57600080fd5b61025d610498565b341561028d57600080fd5b61029561049e565b604051600160a060020a03909116815260200160405180910390f35b34156102bc57600080fd5b6102956104ad565b34156102cf57600080fd5b6101d36104bc565b34156102e257600080fd5b610295610564565b34156102f557600080fd5b6101d3600435610573565b60015433600160a060020a0390811691161461031b5761034c565b600480546001810161032d8382610589565b60009283526020909220018280516103499291602001906105ad565b50505b50565b60005433600160a060020a0390811691161461036a57610378565b600054600160a060020a0316ff5b565b60015433600160a060020a039081169116146103955761034c565b600655565b60005433600160a060020a039081169116146103b55761034c565b600380546001810161032d8382610589565b60055460ff165b90565b6103d961062b565b60038054839081106103e757fe5b90600052602060002090018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104865780601f1061045b57610100808354040283529160200191610486565b820191906000526020600020905b81548152906001019060200180831161046957829003601f168201915b50505050509050919050565b60075490565b60065490565b600154600160a060020a031690565b600054600160a060020a031690565b6104c461062b565b60088054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b5050505050905090565b600254600160a060020a031690565b61057b61062b565b60048054839081106103e757fe5b8154818355818115116103495760008381526020902061034991810190830161063d565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105ee57805160ff191683800117855561061b565b8280016001018555821561061b579182015b8281111561061b578251825591602001919060010190610600565b50610627929150610660565b5090565b60206040519081016040526000815290565b6103ce91905b80821115610627576000610657828261067a565b50600101610643565b6103ce91905b808211156106275760008155600101610666565b50805460018160011615610100020316600290046000825580601f106106a0575061034c565b601f01602090049060005260206000209081019061034c91906106605600a165627a7a723058205a4a2156c0c4c6a1a8c3acb7a245612f47e0a1c1b925e0143d7d51e73cd71a1c0029";

    private Clouser_sol_ClouserContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Clouser_sol_ClouserContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> addToApprovedClouserAsUser(String clouserContent) {
        Function function = new Function(
                "addToApprovedClouserAsUser", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(clouserContent)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setApprovedClouserAmount(BigInteger amount) {
        Function function = new Function(
                "setApprovedClouserAmount", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
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

    public RemoteCall<String> getRequestedClouserByIndex(BigInteger index) {
        Function function = new Function("getRequestedClouserByIndex", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getRequestedClouserAmount() {
        Function function = new Function("getRequestedClouserAmount", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getApprovedClouserAmount() {
        Function function = new Function("getApprovedClouserAmount", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<String> getEncryptedKey() {
        Function function = new Function("getEncryptedKey", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getRequestingProvider() {
        Function function = new Function("getRequestingProvider", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getApprovedClouserByIndex(BigInteger index) {
        Function function = new Function("getApprovedClouserByIndex", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _user, String _requestingProvider, BigInteger _amountRequestedClousers, String _encryptedKey) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Address(_requestingProvider), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountRequestedClousers), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedKey)));
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Clouser_sol_ClouserContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _user, String _requestingProvider, BigInteger _amountRequestedClousers, String _encryptedKey) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.Address(_requestingProvider), 
                new org.web3j.abi.datatypes.generated.Uint256(_amountRequestedClousers), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedKey)));
        return deployRemoteCall(Clouser_sol_ClouserContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Clouser_sol_ClouserContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Clouser_sol_ClouserContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
