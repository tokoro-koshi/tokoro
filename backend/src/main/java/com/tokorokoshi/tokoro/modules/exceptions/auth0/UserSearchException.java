package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to searching users.
 */
public class UserSearchException extends RuntimeException {
    public UserSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}