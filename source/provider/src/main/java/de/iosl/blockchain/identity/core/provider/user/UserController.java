package de.iosl.blockchain.identity.core.provider.user;

import de.iosl.blockchain.identity.core.provider.config.ProviderConfig;
import de.iosl.blockchain.identity.core.provider.user.data.ProviderClaim;
import de.iosl.blockchain.identity.core.provider.user.data.User;
import de.iosl.blockchain.identity.core.provider.user.data.dto.*;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.account.AbstractAuthenticator;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.register.data.dto.RegisterRequestDTO;
import de.iosl.blockchain.identity.core.shared.validator.ECSignatureValidator;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/users")
public class UserController extends AbstractAuthenticator {

    @Autowired
    private UserService userService;
    @Autowired
    private ECSignatureValidator ecSignatureValidator;
    @Autowired
    private ProviderConfig providerConfig;
    @Autowired
    private KeyChain keyChain;

    private EthereumSigner ethereumSigner = new EthereumSigner();

    @GetMapping
    @ApiOperation("Gets all users")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @ApiOperation("Gets a user")
    public UserDTO getUser(@PathVariable("userId") final String userId) {
        return new UserDTO(getUserOrFail(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a new user")
    public UserDTO createUser(@RequestBody @Valid @NonNull UserCreationRequestDTO userRequest) {
        UserDTO userDTO = new UserDTO(
                userRequest.getId(),
                userRequest.getEthId(),
                userRequest.getPublicKey(),
                userRequest.getRegisterContractAddress(),
                userRequest.getClaims().stream().map(this::buildClaimDTO).collect(Collectors.toSet())
        );

        User user = userDTO.toUser(UUID.randomUUID().toString());
        user.getClaims().forEach(this::validateClaim);

        user = userService.insertUser(user);
        return new UserDTO(user);
    }

    @PutMapping("/{userId}")
    @ApiOperation("Updates a user")
    public UserDTO updateUser(@PathVariable("userId") final String userId,
            @RequestBody @Valid @NonNull UserDTO userRequest) {
        if(! userService.exists(userId)) {
            throw new ServiceException("Could not find user with id [%s]", HttpStatus.NOT_FOUND, userId);
        }
        User user = userRequest.toUser(userId);
        user.getClaims().forEach(this::validateClaim);

        user = userService.updateUser(user);
        return new UserDTO(user);
    }

    @DeleteMapping("/{userId}")
    @ApiOperation("Deletes a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") final String userId) {
        User user = getUserOrFail(userId);
        userService.removeUser(user);
    }

    @PostMapping("/{userId}/claim")
    @ApiOperation(value = "Updates or inserts a claim")
    public ClaimDTO createClaim(@PathVariable("userId") final String userId,
            @Valid @RequestBody @NotNull UnsignedClaimDTO unsignedClaimDTO) {
        checkAuthentication();
        User user = getUserOrFail(userId);

        ProviderClaim claim = userService.createClaim(user, new ProviderClaim(buildClaimDTO(unsignedClaimDTO)));
        validateClaim(claim);
        return new ClaimDTO(claim);
    }

    @DeleteMapping("/{userId}/claim/{claimId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes a claim of a user")
    public void removeClaim(@PathVariable("userId") final String userId, @PathVariable("claimId") final String claimId) {
        User user = getUserOrFail(userId);

        userService.removeClaim(user, claimId);
    }

    @PostMapping("/{userId}/register")
    @ApiOperation(
            value = "Registers a users public key and ethereum ID.",
            notes = "Only the state (Government) can post this information.")
    @ResponseStatus(HttpStatus.OK)
    public void registerCredentialInformation(
            @PathVariable("userId") @NotBlank final String userId,
            @RequestBody @Valid @NonNull SignedRequest<RegisterRequestDTO> registerRequest) {

        if (! ecSignatureValidator.isValid(registerRequest, providerConfig.getStateWallet())) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        User user = userService.findUser(userId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND)
        );

        user.setEthId(registerRequest.getPayload().getEthID());
        user.setPublicKey(registerRequest.getPayload().getPublicKey());
        user.setRegisterContractAddress(registerRequest.getPayload().getRegisterContractAddress());

        userService.registerUser(user);
    }

    private User getUserOrFail(@NonNull final String userId) {
        return userService.findUser(userId)
                .orElseThrow(
                        () -> new ServiceException("Could not find user with userId [%s]", HttpStatus.NOT_FOUND, userId)
                );
    }

    @GetMapping("/search")
    @ApiOperation("Search for a user id given query parameter's")
    public List<String> search(
            @RequestParam(value = "givenName", defaultValue = "") String givenName,
            @RequestParam(value = "familyName", defaultValue = "") String familyName) {
        return userService.search(givenName, familyName).stream().map(User::getId).collect(Collectors.toList());
    }

    @GetMapping("/ethID/{ethID}/claimIDs")
    @ApiOperation("Get the claimIDs of a user")
    public Set<ClaimInformationResponse> getClaimInformation(@PathVariable("ethID") @NotBlank String ethID) {
        User user = userService.findUserByEthID(ethID).orElseThrow(
                () -> new ServiceException("Could not find user with ethID [%s]", HttpStatus.NOT_FOUND, ethID)
        );

        return user.getClaims()
                .stream()
                .filter(sharedClaims -> sharedClaims.getId() != null)
                .map(sharedClaim -> new ClaimInformationResponse(
                        sharedClaim.getId(),
                        sharedClaim.getClaimValue().getPayloadType(),
                        sharedClaim.getClaimValue().getPayloadType().getSupportedClaimOperation()
                ))
                .collect(Collectors.toSet());
    }

    private void validateClaim(@NonNull ProviderClaim claim) {
        if(! claim.getClaimValue().getPayloadType().validateType(claim.getClaimValue().getPayload())) {
            throw new ServiceException(
                    "Invalid payload [%s] for payload type [%s].",
                    HttpStatus.BAD_REQUEST,
                    claim.getClaimValue().getPayload(),
                    claim.getClaimValue().getPayloadType());
        }
    }

    private ClaimDTO buildClaimDTO(@NonNull UnsignedClaimDTO unsignedClaimDTO) {
        SignedClaimDTO signedClaimDTO = new SignedClaimDTO(
                keyChain.getAccount().getAddress(),
                unsignedClaimDTO.getId(),
                new Date(),
                unsignedClaimDTO.getProvider(),
                unsignedClaimDTO.getClaimValue()
        );

        ClaimDTO claimDTO = new ClaimDTO(
                new SignedRequest<>(
                        signedClaimDTO,
                        ECSignature.fromSignatureData(ethereumSigner.sign(signedClaimDTO, keyChain.getAccount().getECKeyPair()))
                ),
                null
        );

        return claimDTO;
    }

}
