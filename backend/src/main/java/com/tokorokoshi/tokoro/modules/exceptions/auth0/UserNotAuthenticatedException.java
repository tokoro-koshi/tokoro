package com.tokorokoshi.tokoro.modules.exceptions.auth0;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling errors related to user authentication.
 */
@ResponseStatus(
        value = HttpStatus.UNAUTHORIZED,
        reason = "User not found on auth0"
)
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }

    public UserNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}