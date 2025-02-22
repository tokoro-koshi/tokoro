package com.tokorokoshi.tokoro.database;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
        boolean recommended,
        @Nonnull
        @CreatedDate
        LocalDate createdAt,
        @LastModifiedDate
        LocalDate updatedAt
) {
    /**
     * Creates a new review with the given ID.
     *
     * @param id The ID of the review
     * @return A new review with the given ID
     */
    public Review withId(String id) {
        return new Review(
                id,
                userId,
                placeId,
                comment,
                recommended,
                createdAt,
                updatedAt
        );
    }

    /**
     * Creates a new review with the specified user ID.
     *
     * @param userId The user ID associated with this review
     * @return A new review with the specified user ID
     */
    public Review withUserId(String userId) {
        return new Review(
                id,
                userId,
                placeId,
                comment,
                recommended,
                createdAt,
                updatedAt
        );
    }

    /**
     * Creates a new review with the specified createdAt timestamp.
     *
     * @param createdAt The creation timestamp for this review
     * @return A new review with the specified createdAt timestamp
     */
    public Review withCreatedAt(LocalDate createdAt) {
        return new Review(
                id,
                userId,
                placeId,
                comment,
                recommended,
                createdAt,
                updatedAt
        );
    }
}
