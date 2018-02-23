package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.factories.ClaimFactory;
import de.iosl.blockchain.identity.core.user.claims.claim.dto.UserClaimDTO;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ClaimControllerRestTest extends RestTestSuite {

    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final String claimID_zip = "MAIN_RESIDENT_ZIP_CODE";

    private final ClaimFactory claimFactory = new ClaimFactory();

    @SpyBean
    private KeyChain keyChain;

    private UserClaim birthday;

    @Before
    public void setup() {
        keyChain.setAccount(new Account("asd", null, null, null, null));

        userClaimDB.insert(claimFactory.create(claimID_familyName, ClaimType.STRING, "Wurst", keyChain.getAccount().getAddress()));
        birthday = claimFactory.create(claimID_age, ClaimType.DATE, LocalDateTime.of(2000,04,11,12,12,12), keyChain.getAccount().getAddress());
        userClaimDB.insert(birthday);
        userClaimDB.insert(claimFactory.create(claimID_zip, ClaimType.NUMBER, 3.4, keyChain.getAccount().getAddress()));
    }

    @Test
    public void getClaims() {
        ResponseEntity<List<UserClaimDTO>> claimDTOS = restTemplate.exchange(
                "/claims",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<UserClaimDTO>>() {});

        assertThat(claimDTOS.getBody()).isEqualTo(
                userClaimDB.findAllByEthID("asd")
                        .stream()
                        .map(UserClaimDTO::new)
                        .collect(Collectors.toList()));
    }

    @Test
    public void getClaimsWithClosure() {
        Closure closure = new Closure(
                claimID_age,
                ClaimOperation.EQ,
                new ValueHolder(LocalDateTime.now()),
                keyChain.getAccount().getAddress(),
                false,
                LocalDateTime.now()
        );
        closure.setEthID("0x111");

        SignedRequest<Closure> closureSignedRequest = new SignedRequest<>(
                closure, new ECSignature("r", "s", (byte) 3)
        );

        birthday.setSignedClosures(Lists.newArrayList(closureSignedRequest));

        userClaimDB.insert(birthday);

        ResponseEntity<List<UserClaimDTO>> claimDTOS = restTemplate.exchange(
                "/claims",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<UserClaimDTO>>() {});

        assertThat(claimDTOS.getBody()).isEqualTo(
                userClaimDB.findAllByEthID("asd")
                        .stream()
                        .map(UserClaimDTO::new)
                        .collect(Collectors.toList()));
        UserClaimDTO userClaimDTO = claimDTOS.getBody().stream().filter(uc -> uc.getId().equals(claimID_age)).findAny().get();

        assertThat(userClaimDTO.getSignedClosures()).isNull();

        assertThat(userClaimDTO.getSignedUserClosures()).hasSize(1);
        assertThat(userClaimDTO.getSignedUserClosures().get(0).getBlindedDescription()).isNotEmpty();
        assertThat(userClaimDTO.getSignedUserClosures().get(0).getSignedClosure()).isEqualTo(closureSignedRequest);
    }
}
