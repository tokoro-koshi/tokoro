package com.tokorokoshi.tokoro.modules.favorites.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for a collection
 */
@Schema(
        name = "CollectionDto",
        description = "A DTO for a collection"
)
public record CollectionDto(
        @Schema(
                name = "id",
                description = "The ID of the collection"
        )
        UUID id,

        @Schema(
                name = "name",
                description = "The name of the collection"
        )
        String name,

        @Schema(
                name = "placesIds",
                description = "The list of places IDs in the collection"
        )
        List<String> placesIds,

        @Schema(
                name = "createdAt",
                description = "The date and time the collection was created"
        )
        LocalDateTime createdAt
) {
    /**
     * Creates a new collection with the given ID.
     *
     * @param id The ID of the collection
     * @return A new collection with the given ID
     */
    public CollectionDto withId(UUID id) {
        return new CollectionDto(
                id,
                name,
                placesIds,
                createdAt
        );
    }

    /**
     * Creates a new collection with the given name.
     *
     * @param name The name of the collection
     * @return A new collection with the given name
     */
    public CollectionDto withName(String name) {
        return new CollectionDto(
                id,
                name,
                placesIds,
                createdAt
        );
    }

    /**
     * Creates a new collection with the given list of favorites places.
     *
     * @param placesIds The list of favorites places in the collection
     * @return A new collection with the given list of favorites places
     */
    public CollectionDto withPlacesIds(List<String> placesIds) {
        return new CollectionDto(
                id,
                name,
                placesIds,
                createdAt
        );
    }

    /**
     * Creates a new collection with the specified createdAt timestamp.
     *
     * @param createdAt The creation timestamp for this collection
     * @return A new collection with the specified createdAt timestamp
     */
    public CollectionDto withCreatedAt(LocalDateTime createdAt) {
        return new CollectionDto(
                id,
                name,
                placesIds,
                createdAt
        );
    }
}
