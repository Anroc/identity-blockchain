package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberClosureExpressionTest {

    private Payload payloadDouble;
    private Payload payloadInteger;

    @Before
    public void setup() {
        payloadDouble = new Payload(new ValueHolder(3.4), ClaimType.NUMBER);
        payloadInteger = new Payload(new ValueHolder((double) 18), ClaimType.NUMBER);
    }

    @Test
    public void evaluateDouble_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, 3.4);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateDouble_neq_false() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, -123.3);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression closureExpression = new ClosureExpression(payloadInteger, claimOperation, (double) 18);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_double_neq_false() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression closureExpression = new ClosureExpression(payloadInteger, claimOperation, -123.3);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_double_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression closureExpression = new ClosureExpression(payloadInteger, claimOperation, 18.0);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_gt_true() {
        ClaimOperation claimOperation = ClaimOperation.GT;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, 3.3);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_gt_false() {
        ClaimOperation claimOperation = ClaimOperation.GT;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, 3.4);

        assertThat(closureExpression.evaluate()).isFalse();
    }

    @Test
    public void evaluateInteger_lt_true() {
        ClaimOperation claimOperation = ClaimOperation.LT;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, 3.5);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateInteger_lt_false() {
        ClaimOperation claimOperation = ClaimOperation.LT;

        ClosureExpression closureExpression = new ClosureExpression(payloadDouble, claimOperation, 3.4);

        assertThat(closureExpression.evaluate()).isFalse();
    }

}