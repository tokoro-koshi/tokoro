package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to fetching users.
 */
public class UserFetchException extends RuntimeException {
    public UserFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
