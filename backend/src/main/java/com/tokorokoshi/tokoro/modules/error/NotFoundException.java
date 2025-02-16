package com.tokorokoshi.tokoro.modules.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for 404 Not Found
 */
@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "Resource not found"
)
public class NotFoundException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message The message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
