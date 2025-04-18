package com.tokorokoshi.tokoro.modules.ai;

import com.tokorokoshi.tokoro.helpers.Response;
import jakarta.annotation.Nullable;
import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationResult;

import java.util.List;

/**
 * AI client services facade for our app
 */
public interface AiClientService {
    /**
     * Get a string response from AI model.
     *
     * @param prompt         prompt to send to AI model
     * @param conversationId conversation id
     * @param model          model to use (check language model documentation)
     * @param maxTokens      maximum tokens (check language model documentation)
     * @param temperature    temperature (check language model documentation)
     * @return response from AI model
     * @throws IllegalStateException if prompt is null
     */
    String getResponse(
        String prompt,
        @Nullable String conversationId,
        @Nullable String model,
        @Nullable Integer maxTokens,
        @Nullable Double temperature
    );

    /**
     * Get a structured response from AI model.
     *
     * @param prompt         prompt to send to AI model
     * @param responseType   response type
     * @param conversationId conversation id
     * @param model          model to use (check language model documentation)
     * @param maxTokens      maximum tokens (check language model documentation)
     * @param temperature    temperature (check language model documentation)
     * @param <T>            structured response type
     * @return Response from AI model, which can be successful and contain a generated item,
     * or unsuccessful and contain a string refusal reason
     */
    <T> Response<T> getResponse(
        String prompt,
        Class<T> responseType,
        @Nullable String conversationId,
        @Nullable String model,
        @Nullable Integer maxTokens,
        @Nullable Double temperature
    );

    /**
     * Get a string response from AI model
     *
     * @param messages       initial messages that will seed the context
     * @param conversationId conversation id
     * @param model          model to use (check language model documentation)
     * @param maxTokens      maximum tokens (check language model documentation)
     * @param temperature    temperature (check language model documentation)
     * @return Response from AI model, which can be successful and contain a generated item,
     * or unsuccessful and contain a string refusal reason
     */
    String getResponse(
        List<Message> messages,
        @Nullable String conversationId,
        @Nullable String model,
        @Nullable Integer maxTokens,
        @Nullable Double temperature
    );

    /**
     * Get a structured response from AI model.
     *
     * @param messages       initial messages that will seed the context
     * @param responseType   response type
     * @param conversationId conversation id
     * @param model          model to use (check language model documentation)
     * @param maxTokens      maximum tokens (check language model documentation)
     * @param temperature    temperature (check language model documentation)
     * @param <T>            structured response type
     * @return Response from AI model, which can be successful and contain a generated item,
     * or unsuccessful and contain a string refusal reason
     */
    <T> Response<T> getResponse(
        List<Message> messages,
        Class<T> responseType,
        @Nullable String conversationId,
        @Nullable String model,
        @Nullable Integer maxTokens,
        @Nullable Double temperature
    );

    /**
     * Check request for AI model
     *
     * @param prompt prompt to send to AI model
     * @return moderation response
     */
    Moderation checkRequest(String prompt);

    /**
     * Check if prompt is valid
     *
     * @param prompt prompt to check
     * @return true if prompt is valid, false otherwise
     */
    default boolean isPromptValid(String prompt) {
        return this.checkRequest(prompt)
            .getResults()
            .stream()
            .anyMatch(ModerationResult::isFlagged);
    }
}
