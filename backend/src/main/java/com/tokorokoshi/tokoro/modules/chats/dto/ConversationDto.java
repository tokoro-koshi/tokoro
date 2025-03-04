package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for a conversation
 */
@Schema(
        name = "ConversationDto",
        description = "A DTO for a conversation"
)
public record ConversationDto(
        @Schema(
                name = "id",
                description = "The ID of the conversation"
        )
        String id,

        @Schema(
                name = "prompt",
                description = "The prompt of the conversation"
        )
        String prompt,

        @Schema(
                name = "placesIds",
                description = "The list of places IDs for the conversation"
        )
        List<String> placesIds
) {
}
