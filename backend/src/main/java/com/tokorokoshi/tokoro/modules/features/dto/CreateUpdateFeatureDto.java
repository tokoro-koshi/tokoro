package com.tokorokoshi.tokoro.modules.features.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * A DTO for creating or updating a feature
 */
@Schema(
        name = "CreateUpdateFeatureDto",
        description = "A DTO for creating or updating a feature"
)
public record CreateUpdateFeatureDto(
        @Schema(
                name = "title",
                description = "The title of the feature"
        )
        String title,
        @Schema(
                name = "description",
                description = "The description of the feature"
        )
        String description,
        @Schema(
                name = "picture",
                description = "The picture of the feature"
        )
        MultipartFile picture
) {
}

