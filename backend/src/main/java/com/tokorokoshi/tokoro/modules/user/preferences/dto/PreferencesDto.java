package com.tokorokoshi.tokoro.modules.user.preferences.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing user preferences.
 * This DTO includes fields for language, categories, timezone, and notifications enabled status.
 */
@Schema(
        name = "PreferencesDto",
        description = "Data Transfer Object (DTO) representing user preferences"
)
public record PreferencesDto(
        @Schema(
                name = "language",
                description = "The language preference of the user"
        )
        @NotNull(message = "Language must not be null")
        @NotBlank(message = "Language must not be blank")
        String language,

        @Schema(
                name = "categories",
                description = "The categories of interest to the user"
        )
        @NotNull(message = "Categories must not be null")
        List<
                @NotEmpty(message = "Each category must not be empty")
                @NotBlank(message = "Each category must not be blank")
                        String
                > categories,

        @Schema(
                name = "timezone",
                description = "The timezone preference of the user"
        )
        String timezone,

        @Schema(
                name = "notificationsEnabled",
                description = "The status of notifications enabled for the user",
                defaultValue = "true"
        )
        @NotNull(message = "Notifications enabled status must not be null")
        boolean notificationsEnabled
) {
}