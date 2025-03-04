package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for creating or updating a chat history
 */
@Schema(
        name = "CreateUpdateChatHistoryDto",
        description = "A DTO for creating or updating a chat history"
)
public record CreateUpdateChatHistoryDto(
        @Schema(
                name = "title",
                description = "The title of the chat history"
        )
        String title
) {
}
