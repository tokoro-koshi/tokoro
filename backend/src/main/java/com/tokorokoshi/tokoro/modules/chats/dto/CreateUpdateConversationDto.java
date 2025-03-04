package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for creating or updating a conversation
 */
@Schema(
        name = "CreateUpdateConversationDto",
        description = "A DTO for creating or updating a conversation"
)
public record CreateUpdateConversationDto(
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
