package com.tokorokoshi.tokoro.modules.tags;

import com.tokorokoshi.tokoro.modules.ai.AiClientService;
import com.tokorokoshi.tokoro.modules.ai.Response;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagsService {
    private static final String MODEL = "gpt-4o";
    private static final int TOKENS_LIMIT = 1000;
    private static final double TEMPERATURE = 1.5;

    private final AiClientService clientService;

    @Autowired
    public TagsService(AiClientService clientService) {
        this.clientService = clientService;
    }

    public Response<TagDto> generateTag(String message, String conversationId) {
        return clientService.getResponse(
                message,
                TagDto.class,
                conversationId,
                TagsService.MODEL,
                TagsService.TOKENS_LIMIT,
                TagsService.TEMPERATURE
        );
    }

    public Response<TagsDto> generateTags(String message, String conversationId) {
        return clientService.getResponse(
                message,
                TagsDto.class,
                conversationId,
                TagsService.MODEL,
                TagsService.TOKENS_LIMIT,
                TagsService.TEMPERATURE
        );
    }
}
