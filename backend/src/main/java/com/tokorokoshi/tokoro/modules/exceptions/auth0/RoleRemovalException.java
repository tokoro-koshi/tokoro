package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to removing roles.
 */
public class RoleRemovalException extends RuntimeException {
    public RoleRemovalException(String message, Throwable cause) {
        super(message, cause);
    }
}