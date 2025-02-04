package com.tokorokoshi.tokoro.modules.places.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for a coordinate
 */
@Schema(
        name = "CoordinateDto",
        description = "A DTO for a coordinate"
)
public record CoordinateDto(
        @Schema(
                name = "latitude",
                description = "The latitude of the coordinate"
        )
        double latitude,
        @Schema(
                name = "longitude",
                description = "The longitude of the coordinate"
        )
        double longitude
) {
}
