package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to fetching roles.
 */
public class RoleFetchException extends RuntimeException {
    public RoleFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}