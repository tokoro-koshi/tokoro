package com.tokorokoshi.tokoro.modules.chats.dto;

import com.tokorokoshi.tokoro.database.Message;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
        String userId,

        @Schema(
             name = "messages",
             description = "The list of messages for the chat history"
        )
        List<Message> messages
) {
}
