package com.tokorokoshi.tokoro.modules.places.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for a location
 */
@Schema(
        name = "LocationDto",
        description = "A DTO for a location"
)
public record LocationDto(
        @Schema(
                name = "address",
                description = "The address of the location"
        )
        String address,
        @Schema(
                name = "city",
                description = "The city of the location"
        )
        String city,
        @Schema(
                name = "country",
                description = "The country of the location"
        )
        String country,
        @Schema(
                name = "coordinate",
                description = "The coordinate of the location"
        )
        CoordinateDto coordinate
) {
}
