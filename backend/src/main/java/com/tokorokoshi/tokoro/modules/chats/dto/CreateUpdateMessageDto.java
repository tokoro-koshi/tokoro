package com.tokorokoshi.tokoro.modules.chats.dto;

import com.tokorokoshi.tokoro.database.Message;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO for a message in the chat history
 */
@Schema(
        name = "MessageDto",
        description = "A DTO for creating and updating a message in the chat history"
)
public record CreateUpdateMessageDto(
        @Schema(
                name = "sender",
                description = "The sender of the message (USER or AI)"
        )
        Message.Sender sender,

        @Schema(
                name = "content",
                description = "The content of the message"
        )
        String[] content
) {
}
