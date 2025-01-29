package com.tokorokoshi.tokoro.modules.user.history.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing an entry in the user history.
 * This DTO encapsulates details about an action performed by a user at a specific establishment.
 */
public record HistoryEntryDto (
        @NotNull(message = "Action must not be null")
        @NotBlank(message = "Action must not be blank")
        String action,

        @NotNull(message = "Establishment ID must not be null")
        @NotBlank(message = "Establishment ID must not be blank")
        String establishmentId,

        @NotNull(message = "Timestamp must not be null")
        Date timestamp
) {}
