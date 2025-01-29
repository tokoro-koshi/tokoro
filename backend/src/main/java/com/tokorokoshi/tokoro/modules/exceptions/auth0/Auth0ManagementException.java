package com.tokorokoshi.tokoro.modules.exceptions.auth0;

public class Auth0ManagementException extends RuntimeException {
    public Auth0ManagementException(String message) {
        super(message);
    }

    public Auth0ManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}