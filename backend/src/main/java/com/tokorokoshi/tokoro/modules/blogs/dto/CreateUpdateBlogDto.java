package com.tokorokoshi.tokoro.modules.blogs.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for creating or updating a blog
 */
@Schema(
        name = "CreateUpdateBlogDto",
        description = "A DTO for creating or updating a blog"
)
public record CreateUpdateBlogDto(
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
        List<CommentDto> comments
) {
}
