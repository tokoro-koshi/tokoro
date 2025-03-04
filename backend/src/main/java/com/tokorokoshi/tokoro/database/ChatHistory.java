package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

        @NotNull(message = "Conversation IDs cannot be null")
        List<String> conversationIds,

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
                conversationIds,
                createdAt
        );
    }

    /**
     * Creates a new chat history associated with specified user ID.
     *
     * @param userId The user ID associated with the chat history
     * @return A new chat history with the specified user ID
     */
    public ChatHistory withUserId(String userId) {
        return new ChatHistory(
                id,
                title,
                userId,
                conversationIds,
                createdAt
        );
    }

    /**
     * Creates a new chat history with the specified conversation IDs.
     *
     * @param conversationIds The list of conversation IDs for this chat history
     * @return A new chat history with the specified conversation IDs
     */
    public ChatHistory withConversations(List<String> conversationIds) {
        return new ChatHistory(
                id,
                title,
                userId,
                conversationIds,
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
                conversationIds,
                createdAt
        );
    }
}
