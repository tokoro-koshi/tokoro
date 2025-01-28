package com.tokorokoshi.tokoro.modules.exceptions.history;

public class HistoryEntryNotFoundException extends RuntimeException {
    public HistoryEntryNotFoundException(String message) {
        super(message);
    }
}
