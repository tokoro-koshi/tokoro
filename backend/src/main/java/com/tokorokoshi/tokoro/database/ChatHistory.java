package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an AI chat history in the database.
 */
@Document(collection = "chat_history")
public record ChatHistory(
        @Id
        @NotNull(message = "ID cannot be null")
        String id,

        @NotNull(message = "Title cannot be null")
        @NotBlank(message = "Title cannot be blank")
        @Length(max = 255, message = "Title must be at most 255 characters")
        String title,

        @NotNull(message = "User ID cannot be null")
        String userId,

        @Field
        @NotNull(message = "Messages cannot be null")
        List<Message> messages,

        @CreatedDate
        LocalDateTime createdAt
) {
    /**
     * Creates a new chat history with the specified ID.
     *
     * @param id The ID of the chat history
     * @return A new chat history with the specified ID
     */
    public ChatHistory withId(String id) {
        return new ChatHistory(
                id,
                title,
                userId,
                messages,
                createdAt
        );
    }

    /**
     * Creates a new chat history with the specified messages.
     *
     * @param messages The list of messages for this chat history
     * @return A new chat history with the specified messages
     */
    public ChatHistory withMessages(List<Message> messages) {
        return new ChatHistory(
                id,
                title,
                userId,
                messages,
                createdAt
        );
    }

    /**
     * Creates a new chat history with the specified createdAt timestamp.
     *
     * @param createdAt The creation timestamp for this chat history
     * @return A new chat history with the specified createdAt timestamp
     */
    public ChatHistory withCreatedAt(LocalDateTime createdAt) {
        return new ChatHistory(
                id,
                title,
                userId,
                messages,
                createdAt
        );
    }

    /**
     * Creates a new chat history with the specified chat title.
     *
     * @param title The title for this chat history
     * @return A new chat history with the specified chat title
     */
    public ChatHistory withTitle(String title) {
        return new ChatHistory(
                id,
                title,
                userId,
                messages,
                createdAt
        );
    }
}
