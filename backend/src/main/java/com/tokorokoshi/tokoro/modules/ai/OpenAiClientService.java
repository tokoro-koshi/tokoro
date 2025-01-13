package com.tokorokoshi.tokoro.modules.ai;

import com.tokorokoshi.tokoro.modules.json.JsonHelper;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OpenAiClientService implements AiClientService {
    private final ChatModel chatClient;

    @Autowired
    public OpenAiClientService(ChatModel chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * This builder will generate JSON options for OpenAI fetch query
     * @param responseJsonSchema JSON of schema for GPT to adhere
     *
     * @param conversationId Conversation ID
     * @param model OpenAI Text model
     * @param maxTokens Maximum tokens for processing
     * @param temperature Temperature (randomness, [0, 2])
     * @return Options for OpenAI prompt
     */
    private static OpenAiChatOptions getOptions(
            String responseJsonSchema,
            String conversationId,
            String model,
            Integer maxTokens,
            Double temperature
    ) {
        /*
        {
            "model": "gpt-4o-mini";
            "maxTokens": 1500;
            "chatMemory": {
                "conversationId": "1234";
            };
            "temperature": 0.5;
            "responseFormat"L
        }
         */

        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder();
        if (model != null) {
            optionsBuilder.model(model);
        }
        if (maxTokens != null) {
            optionsBuilder.maxTokens(maxTokens);
        }
        if (conversationId != null) {
            optionsBuilder.toolContext(
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
            optionsBuilder.temperature(temperature);
        }
        if (responseJsonSchema != null) {
            optionsBuilder.responseFormat(new ResponseFormat(
                    ResponseFormat.Type.JSON_SCHEMA,
                    responseJsonSchema
            ));
        }
        return optionsBuilder.build();
    }

    /**
     * This function maps custom messages iterable
     * into OpenAI API compliant classes instances
     *
     * @param messages Messages in chat history
     * @return Messages in chat history suitable for OpenAI API
     */
    private static List<org.springframework.ai.chat.messages.Message> mapMessages(
            Iterable<Message> messages
    ) {
        var messagesList
                = new ArrayList<org.springframework.ai.chat.messages.Message>();
        for (Message message : messages) {
            switch (message.role()) {
                case USER -> messagesList.add(
                        new UserMessage(
                                message.content()
                        )
                );
                case ASSISTANT -> messagesList.add(
                        new AssistantMessage(
                                message.content()
                        )
                );
                case SYSTEM -> messagesList.add(
                        new SystemMessage(
                                message.content()
                        )
                );
                case null, default ->
                        throw new IllegalStateException("Message role was set incorrectly");
            }
        }
        return messagesList;
    }

    @Override
    public String getResponse(
            String strPrompt,
            String conversationId,
            String model,
            Integer maxTokens,
            Double temperature
    ) {
        return this.getResponse(
                List.of(new Message(
                        MessageType.USER,
                        Objects.requireNonNull(strPrompt)
                )),
                conversationId,
                model,
                maxTokens,
                temperature
        );
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
        return this.getResponse(
                List.of(new Message(
                        MessageType.USER,
                        strPrompt)
                ),
                responseType,
                conversationId,
                model,
                maxTokens,
                temperature);
    }

    @Override
    public String getResponse(
            List<Message> messages,
            @Nullable String conversationId,
            @Nullable String model,
            @Nullable Integer maxTokens,
            @Nullable Double temperature
    ) {

        Prompt chatPrompt = new Prompt(
                OpenAiClientService.mapMessages(Objects.requireNonNull(messages)),
                OpenAiClientService.getOptions(
                        null,
                        conversationId,
                        model,
                        maxTokens,
                        temperature
                )
        );

        try {
            return this.chatClient
                    .call(chatPrompt)
                    .getResult()
                    .getOutput()
                    .getContent();
        } catch (Exception e) {
            String[] components = e.getMessage().split(" - ");
            return components.length > 1 ? components[1] : components[0];
        }
    }

    @Override
    public <T> Response<T> getResponse(
            List<Message> messages,
            Class<T> responseType,
            @Nullable String conversationId,
            @Nullable String model,
            @Nullable Integer maxTokens,
            @Nullable Double temperature
    ) {
        Prompt prompt = new Prompt(
                mapMessages(Objects.requireNonNull(messages)),
                OpenAiClientService.getOptions(
                        JsonHelper.
                                getJsonSchema(Objects.requireNonNull(responseType)),
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
                            responseType))
                    .build();
        }catch (Exception e) {
                String[] components = e.getMessage().split(" - ");
                return Response.<T>builder()
                        .conversationId(conversationId)
                        .refusal(
                                components.length > 1
                                        ? components[1]
                                        : e.getMessage()
                        )
                        .refusalStatus(
                                components.length > 1
                                        ? components[0]
                                        : "500"
                        ).build();
        }
    }
}
