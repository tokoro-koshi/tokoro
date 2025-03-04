package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a conversation within a chat history.
 */
@Document(collection = "conversation")
public record Conversation(
        @Id
        @NotNull(message = "ID cannot be null")
        String id,

        @NotNull(message = "Prompt cannot be null")
        @NotBlank(message = "Prompt cannot be blank")
        @Length(max = 1000, message = "Prompt must be at most 1000 characters")
        String prompt,

        @NotNull(message = "Places IDs cannot be null")
        List<String> placesIds
) {
    /**
     * Creates a new conversation with the specified ID.
     *
     * @param id The ID of the conversation
     * @return A new conversation with the specified ID
     */
    public Conversation withId(String id) {
        return new Conversation(
                id,
                prompt,
                placesIds
        );
    }

    /**
     * Creates a new conversation with the specified places IDs.
     *
     * @param placesIds The list of place IDs for the conversation
     * @return A new conversation with the specified places IDs
     */
    public Conversation withPlacesIds(List<String> placesIds) {
        return new Conversation(
                id,
                prompt,
                placesIds
        );
    }
}
