package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a feature section in the database.
 */
@Document(collection = "feature")
public record Feature(
        @Id
        String id,
        @NotNull
        String title,
        @NotNull
        String description,
        @NotNull
        String picture
) {
    /**
     * Creates a new feature with the given ID.
     *
     * @param id The ID of the feature
     * @return A new feature with the given ID
     */
    public Feature withId(String id) {
        return new Feature(
                id,
                title,
                description,
                picture
        );
    }
    /**
     * Creates a new feature with the given picture.
     *
     * @param picture The picture of the feature
     * @return A new feature with the given picture
     */
    public Feature withPicture(String picture) {
        return new Feature(
                id,
                title,
                description,
                picture
        );
    }
}
