package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to user authentication.
 */
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }

    public UserNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}