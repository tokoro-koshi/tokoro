package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.util.List;

public class CreateUpdateBlogDto {
    private String title;
    private String content;
    private List<String> authorId;
    private List<String> tags;
    private List<CommentDto> commentDtos;


    public CreateUpdateBlogDto() {
        this.title = "";
        this.content = "";
        this.authorId = List.of();
        this.tags = List.of();
        this.commentDtos = List.of();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAuthorId() {
        return authorId;
    }

    public void setAuthorId(List<String> authorId) {
        this.authorId = authorId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<CommentDto> getCommentDtos() {
        return commentDtos;
    }

    public void setCommentDtos(List<CommentDto> commentDtos) {
        this.commentDtos = commentDtos;
    }
}
