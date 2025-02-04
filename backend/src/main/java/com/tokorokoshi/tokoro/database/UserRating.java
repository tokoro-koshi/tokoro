package com.tokorokoshi.tokoro.database;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user rating in the database.
 */
@Document(collection = "userRating")
public record UserRating(
        @Id
        String id,
        @Nonnull
        String userId,
        @Nonnull
        String placeId,
        int value
) {
    /**
     * Creates a new user rating with the given value.
     *
     * @param value The value of the rating
     */
    public UserRating {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException(
                    "Rating value must be between 1 and 5"
            );
        }
    }

    /**
     * Creates a new user rating with the given ID.
     *
     * @param id The ID of the user rating
     * @return A new user rating with the given ID
     */
    public UserRating withId(String id) {
        return new UserRating(id, userId, placeId, value);
    }
}
