package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatHistoryMapper {
    @Mapping(target = "messagesIds", expression = "java(new java.util.ArrayList<>())")
    ChatHistory toChatHistoryScheme(CreateUpdateChatHistoryDto createUpdateChatHistoryDto);

    ChatHistoryDto toChatHistoryDto(ChatHistory chatHistory);

    List<ChatHistoryDto> toChatHistoryDto(List<ChatHistory> chatHistories);
}
