package de.iosl.blockchain.identity.core.shared.eba;

import de.iosl.blockchain.identity.core.shared.eba.main.Account;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

public interface EBAInterface {

    /**
     * Creates a new Wallet.
     *
     * @param password encrypt the wallet with the given password
     * @param path saves wallet under the given path
     * @return an {@link Account} object with all the information about the wallet
     */
    Account createWallet(String password, Path path);

    /**
     * Accesses a wallet under the given file path.
     *
     * @param password the password to decrypt the file
     * @param file the wallet file
     * @return an {@link Account} object with all the information about the wallet
     */
    Account accessWallet(String password, File file);

    /**
     * Deploys a register contract for the given account.
     *
     * @param account the account for which the register contract
     * @return the address of the deployed contract
     */
    String deployRegistrarContract(Account account);

    /**
     * Updates the given register contract with the given flag.
     *
     * @param governmentAccount the account with shell be used to update the contract
     * @param contractAddress the address of the register contract
     * @param decision the flag that should be set in the contract
     */
    void setRegisterApproval(Account governmentAccount, String contractAddress, boolean decision);

    /**
     * Returns the current state of the approval of the register contract.
     *
     * @param account the account that request the state
     * @param smartContractAddress the address of the register contract
     * @return the current approval state
     */
    boolean getRegisterApproval(Account account, String smartContractAddress);

    /**
     * Creates a new permission contract
     * @param sender the creator of this permission contract
     * @param recipient the reciepient of the contract
     * @param requestedClaims a set of strings that are the requested claim ids
     * @return the address of this smart contract
     */
    String createPermissionContract(Account sender, String recipient, Set<String> requestedClaims);

    /**
     * Approves the permission contract by putting a signed query into it.
     *
     * @param account the account performing the action
     * @param smartContractAddress the permission contract that shell be approved.
     * @param signedQuery the signed query.
     */
    void approvePermissionContract(Account account, String smartContractAddress, String signedQuery);

    /**
     * Rejects the permission contract.
     *
     * @param account the account performing the action
     * @param smartContractAddress the permission contract that shell be rejected.
     */
    void rejectPermissionContract(Account account, String smartContractAddress);

    /**
     * Registers the given listener to the event that the permission request smart contract gets updated.
     *
     * @param account the account that is registered
     * @param smartContractAddress the smart contract address of the permission contract
     * @param listener the listener that gets executed on an update event
     */
    void registerPermissionContractListener(
            Account account,
            String smartContractAddress,
            PermissionContractListener listener
    );


}
