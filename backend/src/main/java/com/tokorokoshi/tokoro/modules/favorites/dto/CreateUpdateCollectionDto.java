package com.tokorokoshi.tokoro.modules.favorites.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for creating or updating a collection
 */
@Schema(
        name = "CreateUpdateCollectionDto",
        description = "A DTO for creating or updating a collection"
)
public record CreateUpdateCollectionDto(
        @Schema(
                name = "name",
                description = "The name of the collection"
        )
        String name,

        @Schema(
                name = "placeIds",
                description = "The list of place IDs in the collection"
        )
        List<String> placeIds
) {
}
