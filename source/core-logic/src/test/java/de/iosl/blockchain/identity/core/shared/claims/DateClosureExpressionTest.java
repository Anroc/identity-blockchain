package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.closure.ValueHolder;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.ClaimType;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateClosureExpressionTest {

    private Payload payload;

    private final LocalDateTime eq = LocalDateTime.of(2018, 1, 1, 18, 20);
    private final LocalDateTime lt = LocalDateTime.of(2019, 1, 1, 18, 20);
    private final LocalDateTime gt = LocalDateTime.of(2017, 1, 1, 18, 20);

    @Before
    public void setup() {
        payload = new Payload(new ValueHolder(eq), ClaimType.DATE);
    }

    @Test
    public void evaluateDate_eq_true() {
        ClaimOperation claimOperation = ClaimOperation.EQ;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, eq);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateDate_neq_true() {
        ClaimOperation claimOperation = ClaimOperation.NEQ;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, gt);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateDate_gt_true() {
        ClaimOperation claimOperation = ClaimOperation.GT;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, gt);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateDate_gt_false() {
        ClaimOperation claimOperation = ClaimOperation.GT;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, lt);

        assertThat(closureExpression.evaluate()).isFalse();
    }

    @Test
    public void evaluateDate_lt_true() {
        ClaimOperation claimOperation = ClaimOperation.LT;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, lt);

        assertThat(closureExpression.evaluate()).isTrue();
    }

    @Test
    public void evaluateDate_lt_false() {
        ClaimOperation claimOperation = ClaimOperation.LT;

        ClosureExpression<LocalDateTime> closureExpression = new ClosureExpression<>(payload, claimOperation, gt);

        assertThat(closureExpression.evaluate()).isFalse();
    }

}