package com.tokorokoshi.tokoro.modules.tags.dto;

import jakarta.annotation.Nonnull;

import java.util.Arrays;

public class TagsDto {
    @Nonnull
    private TagDto[] tags;

    public TagsDto() {
        this.tags = new TagDto[0];
    }

    public TagsDto(TagDto[] tags) {
        this.tags = tags;
    }

    public TagDto[] getTags() {
        return tags;
    }

    public void setTags(TagDto[] tags) {
        this.tags = tags;
    }

    public Integer count() {
        return tags.length;
    }

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
