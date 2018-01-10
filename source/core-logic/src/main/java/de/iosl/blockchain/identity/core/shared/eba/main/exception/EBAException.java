package de.iosl.blockchain.identity.core.shared.eba.main.exception;

public class EBAException extends RuntimeException {

    public EBAException() {
    }

    public EBAException(String message) {
        super(message);
    }

    public EBAException(String message, Throwable cause) {
        super(message, cause);
    }

    public EBAException(Throwable cause) {
        super(cause);
    }

    public EBAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}