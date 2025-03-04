package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.database.Conversation;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.ConversationDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateConversationDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationDto toConversationDto(Conversation conversation);

    Conversation toConversationScheme(CreateUpdateConversationDto createUpdateConversationDto);

    List<ChatHistoryDto> toChatHistoryDto(List<ChatHistory> chatHistories);
}
