package com.tokorokoshi.tokoro.modules.tag;

import com.tokorokoshi.tokoro.modules.ai.AiClientService;
import com.tokorokoshi.tokoro.modules.ai.Response;
import com.tokorokoshi.tokoro.modules.tag.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tag.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagService {
    private static final String MODEL = "gpt-4o";
    private static final int    TOKENS_LIMIT = 1000;
    private static final double TEMPERATURE = 1.5;

    private final AiClientService clientService;

    @Autowired
    public TagService(AiClientService clientService) {
        this.clientService = clientService;
    }

    public Response<TagDto> generateTag(String message) {
        return clientService.getResponse(
                message,
                TagDto.class,
                null,
                TagService.MODEL,
                TagService.TOKENS_LIMIT,
                TagService.TEMPERATURE
        );
    }

    public Response<TagDto> generateTag(String message, String conversationId) {
        return clientService.getResponse(
                message,
                TagDto.class,
                conversationId,
                TagService.MODEL,
                TagService.TOKENS_LIMIT,
                TagService.TEMPERATURE
        );
    }

    public Response<TagsDto> generateTags(String message) {
        return clientService.getResponse(
                message,
                TagsDto.class,
                null,
                TagService.MODEL,
                TagService.TOKENS_LIMIT,
                TagService.TEMPERATURE
        );
    }

    public Response<TagsDto> generateTags(String message, String conversationId) {
        return clientService.getResponse(
                message,
                TagsDto.class,
                conversationId,
                TagService.MODEL,
                TagService.TOKENS_LIMIT,
                TagService.TEMPERATURE
        );
    }
}
