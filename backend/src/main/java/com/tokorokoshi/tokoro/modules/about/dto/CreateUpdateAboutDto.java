package com.tokorokoshi.tokoro.modules.about.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * A DTO for creating or updating the About information
 */
@Schema(
        name = "CreateUpdateAboutDto",
        description = "A DTO for creating or updating the About information"
)
public record CreateUpdateAboutDto(
        @Schema(
                name = "title",
                description = "The title of the About section"
        )
        String title,
        @Schema(
                name = "subtitle",
                description = "The subtitle of the About section"
        )
        String subtitle,
        @Schema(
                name = "logo",
                description = "The picture of the logo"
        )
        MultipartFile logo,
        @Schema(
                name = "description",
                description = "The description of the application"
        )
        String description,
        @Schema(
                name = "vision",
                description = "The vision statement of the application"
        )
        String vision,
        @Schema(
                name = "mission",
                description = "The mission statement of the application"
        )
        String mission,
        @Schema(
                name = "values",
                description = "The list of core values of the application"
        )
        List<String> values,
        @Schema(
                name = "establishedDate",
                description = "The date the application was established"
        )
        LocalDate establishedDate,
        @Schema(
                name = "socialMedia",
                description = "A map of social media platforms and their URLs"
        )
        Map<String, String> socialMedia
) {
}