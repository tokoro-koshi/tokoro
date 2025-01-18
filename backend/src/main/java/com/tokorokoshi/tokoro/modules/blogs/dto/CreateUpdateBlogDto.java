package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.util.List;

public record CreateUpdateBlogDto(
    String title,
    String content,
    List<String> authorId,
    List<String> tags,
    List<CommentDto> commentDtos
) {
}
