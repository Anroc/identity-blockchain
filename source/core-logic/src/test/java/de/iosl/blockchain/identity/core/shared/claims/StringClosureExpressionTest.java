package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringClosureExpressionTest {

    private Payload payload;

    @Before
    public void setup() {
        payload = new Payload("value", ClaimType.STRING);
    }

    @Test
    public void evaluateString_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression<String> closureExpression = new ClosureExpression<>(payload, claimOperation, "value");

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateString_neq_false() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression<String> closureExpression = new ClosureExpression<>(payload, claimOperation, "asd");

        assertThat(closureExpression.evaluate()).isTrue();
    }
}