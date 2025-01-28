package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to user not found.
 */
public class Auth0UserNotFoundException extends RuntimeException {
    public Auth0UserNotFoundException(String message) {
        super(message);
    }

    public Auth0UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}