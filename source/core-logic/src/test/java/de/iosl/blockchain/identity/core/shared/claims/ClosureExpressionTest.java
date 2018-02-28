package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
public class ClosureExpressionTest {

    @Test
    public void describe() {
        ClosureExpression closureExpression1 = new ClosureExpression(
                new Payload(new ValueHolder(Boolean.TRUE), ClaimType.BOOLEAN),
                ClaimOperation.EQ,
                true);

        ClosureExpression closureExpression2 = new ClosureExpression(
                new Payload(new ValueHolder(LocalDateTime.of(1994, 4, 18, 0, 0)), ClaimType.DATE),
                ClaimOperation.LT,
                LocalDateTime.now().minus(18, ChronoUnit.YEARS));

        log.debug(closureExpression1.describe("IS_DUMP"));
        log.debug(closureExpression2.describe("AGE"));

        assertThat(closureExpression1.describe("IS_DUMP")).isNotNull().isNotEmpty();
        assertThat(closureExpression2.describe("AGE")).isNotNull().isNotEmpty();
    }

    @Test
    public void invalidInput() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> new ClosureExpression(
                        new Payload(new ValueHolder(3), ClaimType.NUMBER),
                        ClaimOperation.EQ,
                        true)
        );
    }
}
