package com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for creating or updating a favorite prompt.
 * This class is used to transfer data between layers of the application,
 * specifically for favorite prompts associated with a user.
 */
public record CreateUpdateFavoritePromptDto(
        @NotNull(message = "Content must not be null")
        @NotBlank(message = "Content must not be blank")
        String content,

        @NotNull(message = "Added date must not be null")
        Date addedAt
) {}