package com.tokorokoshi.tokoro.modules.ratings.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for creating or updating a user rating
 */
@Schema(
        name = "CreateUpdateRatingDto",
        description = "A DTO for creating or updating a user rating"
)
public record CreateUpdateRatingDto(
        @Schema(
                name = "userId",
                description = "The ID of the user"
        )
        String userId,
        @Schema(
                name = "placeId",
                description = "The ID of the place"
        )
        String placeId,
        @Schema(
                name = "value",
                description = "The value of the rating"
        )
        int value
) {
}
