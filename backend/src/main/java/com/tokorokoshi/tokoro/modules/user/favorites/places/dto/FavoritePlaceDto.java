package com.tokorokoshi.tokoro.modules.user.favorites.places.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing a favorite place.
 * This class is used to transfer data between layers of the application,
 * specifically for favorite places associated with a user.
 */
@Schema(
        name = "FavoritePlaceDto",
        description = "Data Transfer Object (DTO) representing a favorite place"
)
public record FavoritePlaceDto(
        @Schema(
                name = "establishmentId",
                description = "The ID of the favorite place"
        )
        @NotNull(message = "Establishment ID must not be null")
        @NotBlank(message = "Establishment ID must not be blank")
        String establishmentId,

        @Schema(
                name = "addedAt",
                description = "The date the favorite place was added"
        )
        Date addedAt,

        @Schema(
                name = "notes",
                description = "The notes about the favorite place"
        )
        String notes,

        @Schema(
                name = "rating",
                description = "The rating of the favorite place"
        )
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must not be greater than 5")
        Integer rating
) {}