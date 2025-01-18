package com.tokorokoshi.tokoro.modules.tags.dto;

import jakarta.annotation.Nonnull;

public record TagDto(
    @Nonnull String lang,
    @Nonnull String name
) {
}
