package com.tokorokoshi.tokoro.modules.exceptions.establishments;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Invalid establishment data provided."
)
public class InvalidEstablishmentException extends IllegalArgumentException {
    public InvalidEstablishmentException(String message) {
        super(message);
    }
}
