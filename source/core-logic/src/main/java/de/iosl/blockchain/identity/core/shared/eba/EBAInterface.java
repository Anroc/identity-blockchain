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
     * @param requesterAddress the address of provider requesting the claims
     * @param requiredClaims a set of strings (claim ids) that are the required by the requesting provider
     * @param optionalClaims a set of strings (claim ids) that are not required by the requesting provider
     * @return the address of this smart contract
     */
    String deployPermissionContract(
            Account sender,
            String recipient,
            String requesterAddress,
            Set<String> requiredClaims,
            Set<String> optionalClaims);

    /**
     * Returns the content of the given permission contract.
     *
     * @param account the account performing the action
     * @param smartContractAddress the address of the permission contract
     * @return the object holding the requested permissions and the requester's address.
     * See {@link #deployPermissionContract(Account, String, String, Set, Set)} for information how to where to find
     * this information
     */
    PermissionContractContent getPermissionContractContent(Account account, String smartContractAddress);

    /**
     * Approves the permission contract by putting a signed query into it.
     *  @param account the account performing the action
     * @param smartContractAddress the permission contract that shell be approved.
     * @param permissionContractContent are the claims that the user explicit approved. This means that the key is the claim id
 *                       and the value is signature over the claimID, user address and requesters address.
     */
    void approvePermissionContract(Account account, String smartContractAddress, PermissionContractContent permissionContractContent);

}
