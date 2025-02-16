package com.tokorokoshi.tokoro.modules.testimonials.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A Data Transfer Object (DTO) for creating or updating testimonial information.
 */
@Schema(
        name = "CreateUpdateTestimonialDto",
        description = "A Data Transfer Object (DTO) for creating or updating testimonial information"
)
public record CreateUpdateTestimonialDto(
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
        String message
) {
}
