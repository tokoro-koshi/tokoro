package com.tokorokoshi.tokoro.modules.exceptions.auth0;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling errors related to searching users.
 */
@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "Resource not found"
)
public class UserSearchException extends RuntimeException {
    public UserSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}