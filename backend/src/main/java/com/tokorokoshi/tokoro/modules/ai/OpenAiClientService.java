package com.tokorokoshi.tokoro.modules.ai;

import com.tokorokoshi.tokoro.modules.json.JsonHelper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAiClientService implements AiClientService {
    private final ChatModel chatClient;

    @Autowired
    public OpenAiClientService(ChatModel chatClient) {
        this.chatClient = chatClient;
    }

    private static OpenAiChatOptions getOptions(
            String responseJsonSchema,
            String conversationId,
            String model,
            Integer maxTokens,
            Double temperature
    ) {
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder();
        if (model != null) {
            optionsBuilder.withModel(model);
        }
        if (maxTokens != null) {
            optionsBuilder.withMaxTokens(maxTokens);
        }
        if (conversationId != null) {
            optionsBuilder.withToolContext(
                    Map.of(
                            "chatMemory",
                            Map.of(
                                    "conversationId",
                                    conversationId
                            )
                    )
            );
        }
        if (temperature != null) {
            optionsBuilder.withTemperature(temperature);
        }
        if (responseJsonSchema != null) {
            optionsBuilder.withResponseFormat(new ResponseFormat(
                    ResponseFormat.Type.JSON_SCHEMA,
                    responseJsonSchema
            ));
        }
        return optionsBuilder.build();
    }


    @Override
    public String getResponse(
            String strPrompt,
            String conversationId,
            String model,
            Integer maxTokens,
            Double temperature
    ) {
        if (strPrompt == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        Prompt prompt = new Prompt(
                strPrompt,
                OpenAiClientService.getOptions(
                        null,
                        conversationId,
                        model,
                        maxTokens,
                        temperature
                )
        );

        try {
            ChatResponse chatResponse = this.chatClient.call(prompt);
            return chatResponse.getResult().getOutput().getContent();
        } catch (Exception e) {
            String[] components = e.getMessage().split(" - ");
            return components.length > 1 ? components[1] : components[0];
        }
    }

    @Override
    public <T> Response<T> getResponse(
            String strPrompt,
            Class<T> responseType,
            String conversationId,
            String model,
            Integer maxTokens,
            Double temperature
    ) {
        if (strPrompt == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (responseType == null) {
            throw new IllegalArgumentException("Response type cannot be null");
        }

        Prompt prompt = new Prompt(
                strPrompt,
                OpenAiClientService.getOptions(
                        JsonHelper.getJsonSchema(responseType),
                        conversationId,
                        model,
                        maxTokens,
                        temperature
                )
        );

        try {
            ChatResponse chatResponse = this.chatClient.call(prompt);
            String strResponse = chatResponse
                    .getResult()
                    .getOutput()
                    .getContent();

            return Response.<T>builder()
                           .conversationId(conversationId)
                           .content(JsonHelper.fromJson(
                                   strResponse,
                                   responseType
                           ))
                           .build();
        } catch (Exception e) {
            String[] components = e.getMessage().split(" - ");
            return Response.<T>builder()
                           .conversationId(conversationId)
                           .refusal(
                                   components.length > 1
                                           ? components[1]
                                           : components[0]
                           )
                           .refusalStatus(
                                   components.length > 1
                                           ? components[0]
                                           : "error"
                           )
                           .build();
        }
    }
}
