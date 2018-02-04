package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BooleanClosureExpressionTest {

    private Payload payloadTrue;
    private Payload payloadFalse;

    @Before
    public void setup() {
        payloadTrue = new Payload(new ValueHolder(Boolean.TRUE), ClaimType.BOOLEAN);
        payloadFalse = new Payload(new ValueHolder(Boolean.FALSE), ClaimType.BOOLEAN);
    }

    @Test
    public void evaluateBoolean_true_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression<Boolean> closureExpression = new ClosureExpression<>(payloadTrue, claimOperation, true);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateBoolean_true_neq_false() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression<Boolean> closureExpression = new ClosureExpression<>(payloadTrue, claimOperation, true);

        assertThat(closureExpression.evaluate()).isFalse();
    }

    @Test
    public void evaluateBoolean_false_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression<Boolean> closureExpression = new ClosureExpression<>(payloadFalse, claimOperation, true);

        assertThat(closureExpression.evaluate()).isFalse();
    }

    @Test
    public void evaluateBoolean_false_neq_false() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression<Boolean> closureExpression = new ClosureExpression<>(payloadFalse, claimOperation, true);

        assertThat(closureExpression.evaluate()).isTrue();
    }
}