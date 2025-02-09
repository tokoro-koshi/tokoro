package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Represents an about section in the database.
 */
@Document(collection = "about")
public record About(
        @Id
        String id,
        @NotNull
        String title,
        String subtitle,
        @NotNull
        String logo,
        @NotNull
        String description,
        String vision,
        String mission,
        List<String> values,
        @NotNull
        LocalDate establishedDate,
        @NotNull
        Map<String, String> socialMedia
) {
    /**
     * Creates a new about with the given ID.
     *
     * @param id The ID of the about
     * @return A new about with the given id
     */
    public About withId(String id) {
        return new About(
                id,
                title,
                subtitle,
                logo,
                description,
                vision,
                mission,
                values,
                establishedDate,
                socialMedia
        );
    }

    /**
     * Creates a new about with the given logo.
     *
     * @param logo The logo of the about
     * @return A new about with the given logo
     */
    public About withLogo(String logo) {
        return new About(
                id,
                title,
                subtitle,
                logo,
                description,
                vision,
                mission,
                values,
                establishedDate,
                socialMedia
        );
    }
}