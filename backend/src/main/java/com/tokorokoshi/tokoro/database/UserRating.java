package com.tokorokoshi.tokoro.database;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
        public UserRating {
                if (value < 1 || value > 5) {
                        throw new IllegalArgumentException("Rating value must be between 1 and 5");
                }
        }

        public UserRating withId(String id) {
                return new UserRating(id, userId, placeId, value);
        }
}
