package com.tokorokoshi.tokoro.database;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a rating in the database.
 */
@Document(collection = "rating")
public record Rating(
        @Id
        String id,
        @Nonnull
        String userId,
        @Nonnull
        String placeId,
        int value
) {
    /**
     * Creates a new rating with the given value.
     *
     * @param value The value of the rating
     */
    public Rating {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException(
                    "Rating value must be between 1 and 5"
            );
        }
    }

    /**
     * Creates a new rating with the given ID.
     *
     * @param id The ID of the rating
     * @return A new rating with the given ID
     */
    public Rating withId(String id) {
        return new Rating(id, userId, placeId, value);
    }
}
