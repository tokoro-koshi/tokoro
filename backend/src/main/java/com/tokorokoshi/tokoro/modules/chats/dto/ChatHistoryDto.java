package com.tokorokoshi.tokoro.modules.chats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for representing chat history data.
 */
@Schema(
        name = "ChatHistoryDto",
        description = "A DTO for representing chat history data"
)
public record ChatHistoryDto(
        @Schema(
                name = "id",
                description = "The ID of the chat history"
        )
        String id,

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
        String userId,

        @Schema(
                name = "messagesIds",
                description = "The list of messages IDs in the chat history"
        )
        List<String> messagesIds,

        @Schema(
                name = "createdAt",
                description = "The creation timestamp of the chat history"
        )
        LocalDateTime createdAt
) {
}
