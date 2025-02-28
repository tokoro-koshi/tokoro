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
                name = "placeId",
                description = "The ID of the place being reviewed"
        )
        String placeId,

        @Schema(
                name = "userId",
                description = "The unique identifier of the user who provided the review"
        )
        String userId,

        @Schema(
                name = "comment",
                description = "The comment of the review"
        )
        String comment,

        @Schema(
                name = "isRecommended",
                description = "Whether the place is recommended by the user"
        )
        boolean isRecommended
) {
}
