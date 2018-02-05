package de.iosl.blockchain.identity.core.shared.claims;

import de.iosl.blockchain.identity.core.shared.claims.data.ClaimOperation;
import de.iosl.blockchain.identity.core.shared.claims.data.Payload;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Getter
public class ClosureExpression<T> {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    private final Payload claim;
    private final ClaimOperation claimOperation;
    private final T value;

    public ClosureExpression(Payload claim, ClaimOperation claimOperation, T value) {
        if(! value.getClass().equals(claim.getPayloadType().getClazz())) {
            throw new IllegalArgumentException("Incompatible closure types.");
        }

        if(! isValidOperation(claim, claimOperation)) {
            throw new IllegalArgumentException("Claim does not support given claimOperation.");
        }

        this.claim = claim;
        this.claimOperation = claimOperation;
        this.value = value;
    }

    private final boolean isValidOperation(Payload claim, ClaimOperation op) {
        return Arrays.stream(claim.getPayloadType().getSupportedClaimOperation())
                .filter(supportedClaimOperation -> supportedClaimOperation == op)
                .findAny()
                .isPresent();
    }

    public boolean evaluate() {
        switch (claim.getPayloadType()) {
            case DATE:
                return evaluateDate();
            case NUMBER:
                return evaluateNumber();
            case STRING:
                return evaluateString();
            case BOOLEAN:
                return evaluateBoolean();
            case OBJECT:
            default:
                throw new UnsupportedOperationException("Operations on this type are not supported");
        }
    }

    public String describe(String claimId) {
        Object printablePayload = getClaim().getPayload().getUnifiedValue();
        Object printableValue = value;
        if(getClaim().getPayload().getUnifiedValue() instanceof LocalDateTime) {
            printablePayload = DATE_TIME_FORMATTER.format((LocalDateTime) getClaim().getPayload().getUnifiedValue());
            printableValue = DATE_TIME_FORMATTER.format((LocalDateTime) value);
        }
        String parsedClaimId = claimId.toLowerCase().replaceAll("_", " ");
        return String.format("Is the claim \"%s\" (your value is \"%s\") %s \"%s\"?", parsedClaimId, printablePayload, claimOperation.getDescription(), printableValue);
    }

    private boolean evaluateDate() {
        LocalDateTime localDateTime = claim.getPayloadAsDate();
        LocalDateTime localDateTimeValue = (LocalDateTime) value;
        switch (claimOperation) {
            case EQ:
                return localDateTime.isEqual(localDateTimeValue);
            case NEQ:
                return ! localDateTime.isEqual(localDateTimeValue);
            case GT:
                return localDateTime.isAfter(localDateTimeValue);
            case GE:
                return localDateTime.isAfter(localDateTimeValue) || localDateTime.isEqual(localDateTimeValue);
            case LT:
                return localDateTime.isBefore(localDateTimeValue);
            case LE:
                return localDateTime.isBefore(localDateTimeValue) || localDateTime.isEqual(localDateTimeValue);
            default:
                throw new UnsupportedOperationException("Operations on this type are not supported");
        }
    }

    private boolean evaluateNumber() {
        double doubleClaim = claim.getPayloadAsDouble();
        double doubleClaimValue = (Double) value;
        switch (claimOperation) {
            case EQ:
                return doubleClaim == doubleClaimValue;
            case NEQ:
                return doubleClaim != doubleClaimValue;
            case GT:
                return doubleClaim > doubleClaimValue;
            case GE:
                return doubleClaim >= doubleClaimValue;
            case LT:
                return doubleClaim < doubleClaimValue;
            case LE:
                return doubleClaim <= doubleClaimValue;
            default:
                throw new UnsupportedOperationException("Operations on this type are not supported");
        }
    }

    private boolean evaluateString() {
        String stringClaim = claim.getPayloadAsString();
        String stringClaimValue = (String) value;
        switch (claimOperation) {
            case EQ:
                return stringClaim.equals(stringClaimValue);
            case NEQ:
                return ! stringClaim.equals(stringClaimValue);
            default:
                throw new UnsupportedOperationException("Operations on this type are not supported");
        }
    }

    private boolean evaluateBoolean() {
        boolean booleanClaim = claim.getPayloadAsBoolean();
        boolean booleanClaimValue = (Boolean) value;
        switch (claimOperation) {
            case EQ:
                return booleanClaim == booleanClaimValue;
            case NEQ:
                return booleanClaim != booleanClaimValue;
            default:
                throw new UnsupportedOperationException("Operations on this type are not supported");
        }
    }
}
