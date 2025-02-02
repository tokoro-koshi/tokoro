package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.time.Instant;
import java.util.List;

public record BlogDto(
    String id,
    String title,
    String content,
    List<String> authorId,
    List<String> tags,
    List<CommentDto> comments,
    Instant createdAt,
    Instant updatedAt
) {
}
