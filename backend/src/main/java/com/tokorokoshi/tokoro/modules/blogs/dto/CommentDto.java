package com.tokorokoshi.tokoro.modules.blogs.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * A DTO for a comment
 */
@Schema(
        name = "CommentDto",
        description = "A DTO for a comment"
)
public record CommentDto(
        @Schema(
                name = "id",
                description = "The ID of the comment"
        )
        String userId,
        @Schema(
                name = "value",
                description = "The value of the comment"
        )
        String value,
        @Schema(
                name = "createdAt",
                description = "The time the comment was created"
        )
        Instant createdAt,
        @Schema(
                name = "updatedAt",
                description = "The time the comment was last updated"
        )
        Instant updatedAt
) {
}
