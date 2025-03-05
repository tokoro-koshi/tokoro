package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a message in the chat history.
 */
@Document
public record Message(
        @NotNull(message = "Sender cannot be null")
        Sender sender,

        @NotNull(message = "Content cannot be null")
        @NotEmpty(message = "Content cannot be empty")
        String[] content
) {
    /**
     * Enum representing the sender of the message.
     */
    public enum Sender {
        USER, // Indicates the message is from the user (always one message)
        AI // Indicates the message is from the artificial intelligence provider (array of places IDs)
    }
}
