package com.tokorokoshi.tokoro.modules.privacy.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for the 'Contact us' information
 */
@Schema(
        name = "ContactUsDto",
        description = "A DTO for the 'Contact us' information"
)
public record ContactUsDto(
        @Schema(
                name = "socialLinks",
                description = "List of social media links"
        )
        List<String> socialLinks,
        @Schema(
                name = "phoneNumber",
                description = "Phone number for privacy inquiries"
        )
        String phoneNumber
) {
}
