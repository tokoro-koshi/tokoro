package com.tokorokoshi.tokoro.modules.ai;

import org.springframework.ai.chat.messages.MessageType;

/**
 * A message that can be sent to the user, assistant, or system.
 */
public record Message(MessageType role, String content) {
    /**
     * Create a system message.
     *
     * @param content The content of the message
     * @return The message
     */
    public static Message systemMessage(String content) {
        return new Message(MessageType.SYSTEM, content);
    }

    /**
     * Create a user message.
     *
     * @param content The content of the message
     * @return The message
     */
    public static Message userMessage(String content) {
        return new Message(MessageType.USER, content);
    }

    /**
     * Create an assistant message.
     *
     * @param content The content of the message
     * @return The message
     */
    public static Message assistantMessage(String content) {
        return new Message(MessageType.ASSISTANT, content);
    }

    /**
     * Create a tool message.
     *
     * @param content The content of the message
     * @return The message
     */
    public static Message toolMessage(String content) {
        return new Message(MessageType.TOOL, content);
    }
}
