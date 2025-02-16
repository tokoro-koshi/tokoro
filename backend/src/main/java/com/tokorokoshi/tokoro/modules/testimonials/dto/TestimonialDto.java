package com.tokorokoshi.tokoro.modules.testimonials.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * A Data Transfer Object (DTO) for representing testimonial information.
 */
@Schema(
        name = "TestimonialDto",
        description = "A Data Transfer Object (DTO) for representing testimonial information"
)
public record TestimonialDto(
        @Schema(
                name = "id",
                description = "The unique identifier for the testimonial"
        )
        String id,

        @Schema(
                name = "userId",
                description = "The user ID associated with the testimonial"
        )
        String userId,

        @Schema(
                name = "rating",
                description = "The rating given in the testimonial (between 1 and 5)",
                minimum = "1",
                maximum = "5"
        )
        int rating,

        @Schema(
                name = "message",
                description = "The message content of the testimonial"
        )
        String message,

        @Schema(
                name = "createdAt",
                description = "The date when the testimonial was created"
        )
        LocalDate createdAt
) {
}