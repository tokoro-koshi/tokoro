package com.tokorokoshi.tokoro.modules.blogs.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * A DTO for a blog
 */
@Schema(
        name = "BlogDto",
        description = "A DTO for a blog"
)
public record BlogDto(
        @Schema(
                name = "id",
                description = "The ID of the blog"
        )
        String id,
        @Schema(
                name = "title",
                description = "The title of the blog"
        )
        String title,
        @Schema(
                name = "content",
                description = "The content of the blog"
        )
        String content,
        @Schema(
                name = "authorId",
                description = "The ID of the author of the blog"
        )
        List<String> authorId,
        @Schema(
                name = "tags",
                description = "The tags of the blog"
        )
        List<String> tags,
        @Schema(
                name = "comments",
                description = "The comments on the blog"
        )
        List<CommentDto> comments,
        @Schema(
                name = "createdAt",
                description = "The time the blog was created"
        )
        Instant createdAt,
        @Schema(
                name = "updatedAt",
                description = "The time the blog was last updated"
        )
        Instant updatedAt
) {
}
