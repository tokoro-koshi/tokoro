package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for a chat history
 */
@Schema(
        name = "ChatHistoryDto",
        description = "A DTO for a chat history"
)
public record ChatHistoryDto(
        @Schema(
                name = "id",
                description = "The ID of the chat history"
        )
        String id,

        @Schema(
                name = "title",
                description = "The title of the chat history"
        )
        String title,

        @Schema(
                name = "userId",
                description = "The user ID associated with this chat history"
        )
        String userId,

        @Schema(
                name = "conversationsIds",
                description = "The list of conversations IDs for this chat history"
        )
        List<String> conversationsIds,

        @Schema(
                name = "createdAt",
                description = "The creation timestamp for this chat history"
        )
        LocalDateTime createdAt
) {
}
