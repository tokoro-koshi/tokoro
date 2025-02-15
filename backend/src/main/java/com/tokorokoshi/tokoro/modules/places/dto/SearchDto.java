package com.tokorokoshi.tokoro.modules.places.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SearchDto",
        description = "A DTO for searching places"
)
public record SearchDto(
        @Schema(
                name = "prompt",
                description = "A user prompt for AI to search places"
        )
        String prompt
) {
}
