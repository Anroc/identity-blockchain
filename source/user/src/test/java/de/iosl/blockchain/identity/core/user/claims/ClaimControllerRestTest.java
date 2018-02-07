package de.iosl.blockchain.identity.core.user.claims;

import de.iosl.blockchain.identity.core.RestTestSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.user.Application;
import de.iosl.blockchain.identity.core.user.claims.claim.UserClaim;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ClaimControllerRestTest extends RestTestSuite {

    private final String claimID_familyName = "FAMILY_NAME";
    private final String claimID_age = "BIRTHDAY";
    private final String claimID_zip = "MAIN_RESIDENT_ZIP_CODE";

    @SpyBean
    private KeyChain keyChain;

    @Before
    public void setup() {
        keyChain.setAccount(new Account("asd", null, null, null, null));

        userClaimDB.insert(new UserClaim(claimID_familyName, new Date(), new Provider("0x111", "gov"), new Payload(new ValueHolder("Wurst"), ClaimType.STRING), keyChain.getAccount().getAddress()));
        userClaimDB.insert(new UserClaim(claimID_age, new Date(), new Provider("0x111", "gov"), new Payload(new ValueHolder(
                LocalDateTime.of(2000,04,11,12,12,12)), ClaimType.DATE), keyChain.getAccount().getAddress()));
        userClaimDB.insert(new UserClaim(claimID_zip, new Date(), new Provider("0x111", "gov"), new Payload(new ValueHolder(3.4), ClaimType.NUMBER), keyChain.getAccount().getAddress()));
    }

    @Test
    public void getClaims() {
        ResponseEntity<List<ClaimDTO>> claimDTOS = restTemplate.exchange(
                "/claims",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ClaimDTO>>() {});

        assertThat(claimDTOS.getBody()).isEqualTo(
                userClaimDB.findAllByEthID("asd")
                        .stream()
                        .map(ClaimDTO::new)
                        .collect(Collectors.toList()));
    }
}
