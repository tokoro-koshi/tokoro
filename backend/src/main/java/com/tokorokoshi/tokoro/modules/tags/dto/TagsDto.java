package com.tokorokoshi.tokoro.modules.tags.dto;

import jakarta.annotation.Nonnull;

import java.util.Arrays;

public record TagsDto(
        @Nonnull TagDto[] tags
) {
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
