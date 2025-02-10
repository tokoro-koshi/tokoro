package com.tokorokoshi.tokoro.modules.features.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for a feature
 */
@Schema(
        name = "FeatureDto",
        description = "A DTO for a feature"
)
public record FeatureDto(
        @Schema(
                name = "id",
                description = "The ID of the feature"
        )
        String id,
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
        String picture
) {
}