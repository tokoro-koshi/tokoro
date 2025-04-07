package com.tokorokoshi.tokoro.modules.tags;

import com.tokorokoshi.tokoro.modules.ai.AiClientService;
import com.tokorokoshi.tokoro.dto.Response;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TagsService {
    private static final String MODEL = "gpt-4o-mini";
    private static final int TOKENS_LIMIT = 2000;
    private static final double TEMPERATURE = 1.0;

    private final AiClientService clientService;

    @Autowired
    public TagsService(AiClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Generates tags for the given message.
     *
     * @param message        message to generate tags for
     * @param conversationId conversation ID
     * @param temperature Temperature of AI response
     * @return response with the tags
     */
    public Response<TagsDto> generateTags(
        String message,
        String conversationId,
        double temperature
    ) {
        boolean isInappropriate = clientService.isPromptValid(message);
        if (isInappropriate) {
            return Response.<TagsDto>builder()
                           .refusal("Message contains inappropriate content")
                           .refusalStatus(HttpStatus.BAD_REQUEST.value())
                           .build();
        }

        return clientService.getResponse(
            TagsPromptEnhancer.enhancePrompt(message),
            TagsDto.class,
            conversationId,
            TagsService.MODEL,
            TagsService.TOKENS_LIMIT,
            temperature
        );
    }

    /**
     * Generates tags for the given message.
     *
     * @param message        message to generate tags for
     * @param conversationId conversation ID
     * @return response with the tags
     */
    public Response<TagsDto> generateTags(
        String message,
        String conversationId
    ) {
        return generateTags(message, conversationId, TagsService.TEMPERATURE);
    }

    /**
     * Generates tags for the given message.
     *
     * @param message message to generate tags for
     * @param temperature Temperature of AI response
     * @return response with the tags
     */
    public Response<TagsDto> generateTags(String message, double temperature) {
        return generateTags(message, null, temperature);
    }

    /**
     * Generates tags for the given message.
     *
     * @param message message to generate tags for
     * @return response with the tags
     */
    public Response<TagsDto> generateTags(String message) {
        return generateTags(message, null);
    }
}
