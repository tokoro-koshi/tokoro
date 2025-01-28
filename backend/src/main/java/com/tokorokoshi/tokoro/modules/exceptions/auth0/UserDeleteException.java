package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to deleting users.
 */
public class UserDeleteException extends RuntimeException {
    public UserDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
