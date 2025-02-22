package com.tokorokoshi.tokoro.modules.reviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * A DTO for a review
 */
@Schema(
        name = "ReviewDto",
        description = "A DTO for a review"
)
public record ReviewDto(
        @Schema(
                name = "id",
                description = "The ID of the review"
        )
        String id,
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
                name = "createdAt",
                description = "The date and time the review was created"
        )
        LocalDate createdAt
) {
}