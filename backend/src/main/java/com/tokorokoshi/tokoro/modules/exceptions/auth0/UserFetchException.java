package com.tokorokoshi.tokoro.modules.exceptions.auth0;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling errors related to fetching users.
 */
@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "Resource not found"
)
public class UserFetchException extends RuntimeException {
    public UserFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
