package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.time.Instant;

public record CommentDto(
        String userId,
        String value,
        Instant createdAt,
        Instant updatedAt
) {
}
