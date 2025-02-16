package com.tokorokoshi.tokoro.modules.testimonials.dto;

import com.tokorokoshi.tokoro.database.Testimonial;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A Data Transfer Object (DTO) for updating the status of a Testimonial.
 */
@Schema(
        name = "UpdateTestimonialStatusDto",
        description = "A Data Transfer Object (DTO) for updating the status of a Testimonial"
)
public record UpdateTestimonialStatusDto(
        @Schema(
                name = "status",
                description = "The new status for the testimonial",
                allowableValues = {"PENDING", "APPROVED", "REJECTED"}
        )
        Testimonial.Status status
) {
}
