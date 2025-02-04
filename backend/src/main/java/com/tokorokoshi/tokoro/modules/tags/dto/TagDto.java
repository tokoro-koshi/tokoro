package com.tokorokoshi.tokoro.modules.tags.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

/**
 * A DTO for a tag
 */
@Schema(
        name = "TagDto",
        description = "A DTO for a tag"
)
public record TagDto(
        @Schema(
                name = "lang",
                description = "The language of the tag"
        )
        @Nonnull String lang,
        @Schema(
                name = "name",
                description = "The payload of the tag - name or a short description"
        )
        @Nonnull String name
) {
}
