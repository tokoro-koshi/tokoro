package com.tokorokoshi.tokoro.modules.exceptions.preferences;

public class InvalidPreferenceException extends IllegalArgumentException {
    public InvalidPreferenceException(String message) {
        super(message);
    }
}
