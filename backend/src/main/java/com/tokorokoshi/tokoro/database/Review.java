package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Represents a review in the database.
 */
@Document(collection = "review")
public record Review(
        @Id
        @NotNull(message = "ID cannot be null")
        String id,

        @NotNull(message = "User ID cannot be null")
        String userId,
        @NotNull(message = "Place ID cannot be null")
        String placeId,

        @NotNull(message = "Comment cannot be null")
        @NotBlank(message = "Comment cannot be blank")
        @Length(max = 300, message = "Comment must be at most 300 characters")
        String comment,
        boolean isRecommended,

        @CreatedDate
        Instant createdAt,

        @LastModifiedDate
        Instant updatedAt
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
                isRecommended,
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
                isRecommended,
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
    public Review withCreatedAt(Instant createdAt) {
        return new Review(
                id,
                userId,
                placeId,
                comment,
                isRecommended,
                createdAt,
                updatedAt
        );
    }
}
