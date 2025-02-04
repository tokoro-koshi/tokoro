package com.tokorokoshi.tokoro.modules.tags.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.Arrays;

/**
 * A DTO for a list of tags
 */
@Schema(
        name = "TagsDto",
        description = "A DTO for a list of tags"
)
public record TagsDto(
        @Schema(
                name = "tags",
                description = "The tags this DTO represents"
        )
        @Nonnull
        TagDto[] tags
) {
    /**
     * Get the number of tags in the DTO.
     *
     * @return The number of tags in the DTO
     */
    public Integer count() {
        return tags.length;
    }

    /**
     * Convert the DTO to a debug string.
     */
    @Override
    public String toString() {
        return "Tags (%s): [%s]".formatted(
                this.count(),
                String.join(
                        ", ",
                        Arrays.stream(tags)
                              .map(TagDto::toString)
                              .toArray(String[]::new)
                )
        );
    }
}
