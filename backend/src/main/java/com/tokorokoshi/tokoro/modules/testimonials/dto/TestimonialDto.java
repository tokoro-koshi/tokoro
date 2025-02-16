package com.tokorokoshi.tokoro.modules.testimonials.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * A DTO for the Testimonial information
 */
@Schema(
        name = "TestimonialDto",
        description = "A DTO for the Testimonial information"
)
public record TestimonialDto(
        @Schema(
                name = "id",
                description = "The ID of the testimonial"
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