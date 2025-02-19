package com.tokorokoshi.tokoro.modules.exceptions.auth0;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling errors related to user not found.
 */
@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "User not found"
)
public class Auth0UserNotFoundException extends RuntimeException {
    public Auth0UserNotFoundException(String message) {
        super(message);
    }

    public Auth0UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}