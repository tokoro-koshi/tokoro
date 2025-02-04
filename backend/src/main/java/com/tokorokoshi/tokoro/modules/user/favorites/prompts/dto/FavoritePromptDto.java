package com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing a favorite prompt.
 * This class is used to transfer data between layers of the application,
 * specifically for favorite prompts associated with a user.
 */
@Schema(
        name = "FavoritePromptDto",
        description = "Data Transfer Object (DTO) representing a favorite prompt"
)
public record FavoritePromptDto(
        @Schema(
                name = "promptId",
                description = "The ID of the favorite prompt"
        )
        @NotNull(message = "Prompt ID must not be null")
        @NotBlank(message = "Prompt ID must not be blank")
        String promptId,

        @Schema(
                name = "content",
                description = "The content of the favorite prompt"
        )
        @NotNull(message = "Content must not be null")
        @NotBlank(message = "Content must not be blank")
        String content,

        @Schema(
                name = "addedAt",
                description = "The date the favorite prompt was added"
        )
        Date addedAt
) {
}
