package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for creating and updating chat histories.
 */
@Schema(
        name = "CreateUpdateChatHistoryDto",
        description = "A DTO for creating and updating chat histories"
)
public record CreateUpdateChatHistoryDto(
        @Schema(
                name = "title",
                description = "The title of the chat history",
                maxLength = 255
        )
        String title,

        @Schema(
                name = "userId",
                description = "The user ID associated with the chat history"
        )
        String userId
) {
}
