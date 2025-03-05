package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.Message;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateMessageDto;
import com.tokorokoshi.tokoro.modules.chats.dto.MessageDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessageScheme(CreateUpdateMessageDto createUpdateMessageDto);

    MessageDto toMessageDto(Message message);

    List<MessageDto> toMessageDto(List<Message> chatHistories);
}
