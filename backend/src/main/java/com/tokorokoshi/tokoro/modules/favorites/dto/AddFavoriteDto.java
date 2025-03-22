package com.tokorokoshi.tokoro.modules.favorites.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AddFavoriteDto",
        description = "A DTO for adding a favorite record"
)
public record AddFavoriteDto(
        @Schema(
                name = "placeId",
                description = "The ID of the place to add to favorites"
        )
        String placeId
) {}
