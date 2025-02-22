package com.tokorokoshi.tokoro.database;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a review in the database.
 */
@Document(collection = "review")
public record Review(
        @Id
        String id,
        @Nonnull
        String userId,
        @Nonnull
        String placeId,
        @Nonnull
        String comment,
        @Nonnull
        @CreatedDate
        LocalDate createdAt
) {
    /**
     * Creates a new review with the given ID.
     *
     * @param id The ID of the review
     * @return A new review with the given ID
     */
    public Review withId(String id) {
        return new Review(id, userId, placeId, comment, createdAt);
    }
}
