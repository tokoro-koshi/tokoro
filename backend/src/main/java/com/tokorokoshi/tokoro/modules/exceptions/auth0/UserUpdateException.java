package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to updating users.
 */
public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}