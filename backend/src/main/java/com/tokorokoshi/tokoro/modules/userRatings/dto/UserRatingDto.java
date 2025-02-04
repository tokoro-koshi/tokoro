package com.tokorokoshi.tokoro.modules.userRatings.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for a user rating
 */
@Schema(
        name = "UserRatingDto",
        description = "A DTO for a user rating"
)
public record UserRatingDto(
        @Schema(
                name = "id",
                description = "The ID of the user rating"
        )
        String id,
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
