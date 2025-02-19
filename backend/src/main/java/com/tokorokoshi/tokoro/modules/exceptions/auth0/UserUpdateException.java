package com.tokorokoshi.tokoro.modules.exceptions.auth0;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling errors related to updating users.
 */
@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Error updating user"
)
public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}