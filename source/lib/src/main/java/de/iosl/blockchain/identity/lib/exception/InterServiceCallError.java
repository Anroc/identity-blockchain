package de.iosl.blockchain.identity.lib.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Data
public class InterServiceCallError extends ServiceException {

    private final Object data;

    public InterServiceCallError(HttpStatus statusCode, String reason, Object data) {
        super(reason, statusCode);
        this.data = data;
    }
}
