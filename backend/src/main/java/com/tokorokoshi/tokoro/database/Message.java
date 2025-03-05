package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a message in the chat history.
 */
@Document
public record Message(
        @Id
        @NotNull(message = "ID cannot be null")
        String id,
        
        @NotNull(message = "Sender cannot be null")
        Sender sender,

        @NotNull(message = "Content cannot be null")
        @NotEmpty(message = "Content cannot be empty")
        String[] content
) {
    /**
     * Creates a new message in chat history with the specified ID.
     *
     * @param id The ID of the message
     * @return A new message in chat history with the specified ID
     */
    public Message withId(String id) {
        return new Message(
                id,
                sender,
                content
        );
    }
    /**
     * Enum representing the sender of the message.
     */
    public enum Sender {
        USER, // Indicates the message is from the user (always one message)
        AI // Indicates the message is from the artificial intelligence provider (array of places IDs)
    }
}
