package com.tokorokoshi.tokoro.modules.reviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for creating or updating a review
 */
@Schema(
        name = "CreateUpdateReviewDto",
        description = "A DTO for creating or updating a review"
)
public record CreateUpdateReviewDto(
        @Schema(
                name = "userId",
                description = "The ID of the user who wrote the review"
        )
        String userId,
        @Schema(
                name = "placeId",
                description = "The ID of the place being reviewed"
        )
        String placeId,
        @Schema(
                name = "comment",
                description = "The comment of the review"
        )
        String comment,
        @Schema(
                name = "recommended",
                description = "Whether the place is recommended by the user"
        )
        boolean recommended
) {
}