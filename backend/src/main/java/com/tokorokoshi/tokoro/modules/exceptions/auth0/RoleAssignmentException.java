package com.tokorokoshi.tokoro.modules.exceptions.auth0;

/**
 * Custom exception class for handling errors related to assigning roles.
 */
public class RoleAssignmentException extends RuntimeException {
    public RoleAssignmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
