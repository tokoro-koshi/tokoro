package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatHistoryMapper {
    ChatHistory toChatHistoryScheme(CreateUpdateChatHistoryDto createUpdateChatHistoryDto);

    ChatHistoryDto toChatHistoryDto(ChatHistory chatHistory);

    List<ChatHistoryDto> toChatHistoryDto(List<ChatHistory> chatHistories);
}
