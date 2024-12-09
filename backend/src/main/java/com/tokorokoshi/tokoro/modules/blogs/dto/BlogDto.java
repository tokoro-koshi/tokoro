package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.time.Instant;
import java.util.List;

public class BlogDto {
    private String id;
    private String title;
    private String content;
    private List<String> authorId;
    private List<String> tags;
    private List<CommentDto> commentDtos;
    private Instant createdAt;
    private Instant updatedAt;


    public BlogDto(){
        this.id = "";
        this.title = "";
        this.content = "";
        this.authorId = List.of();
        this.tags = List.of();
        this.commentDtos = List.of();
        this.createdAt = null;
        this.updatedAt = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
