package com.tokorokoshi.tokoro.modules.user.history.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing an entry in the user history.
 * This DTO encapsulates details about an action performed by a user at a specific establishment.
 */
@Schema(
        name = "HistoryEntryDto",
        description = "Data Transfer Object (DTO) representing an entry in the user history"
)
public record HistoryEntryDto (
        @Schema(
                name = "action",
                description = "The action performed by the user"
        )
        @NotNull(message = "Action must not be null")
        @NotBlank(message = "Action must not be blank")
        String action,

        @Schema(
                name = "establishmentId",
                description = "The ID of the establishment where the action was performed"
        )
        @NotNull(message = "Establishment ID must not be null")
        @NotBlank(message = "Establishment ID must not be blank")
        String establishmentId,

        @Schema(
                name = "timestamp",
                description = "The date and time the action was performed"
        )
        Date timestamp
) {}
