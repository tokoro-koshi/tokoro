package com.tokorokoshi.tokoro.modules.exceptions.history;

public class NoHistoryEntriesException extends RuntimeException {
    public NoHistoryEntriesException(String message) {
        super(message);
    }
}
