package com.tokorokoshi.tokoro.modules.user.preferences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing user preferences.
 * This DTO includes fields for language, categories, timezone, and notifications enabled status.
 */
public record PreferencesDto(
        @NotNull(message = "Language must not be null")
        @NotBlank(message = "Language must not be blank")
        String language,

        @NotNull(message = "Categories must not be null")
        List<
                @NotEmpty(message = "Each category must not be empty")
                @NotBlank(message = "Each category must not be blank")
                        String
                > categories,

        String timezone,

        @NotNull(message = "Notifications enabled must not be null")
        boolean notificationsEnabled
) {
}