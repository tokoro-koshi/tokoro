package com.tokorokoshi.tokoro.modules.user.favorites.places.dto;

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
public record FavoritePlaceDto(
        @NotNull(message = "Establishment ID must not be null")
        @NotBlank(message = "Establishment ID must not be blank")
        String establishmentId,

        Date addedAt,

        String notes,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must not be greater than 5")
        Integer rating
) {}