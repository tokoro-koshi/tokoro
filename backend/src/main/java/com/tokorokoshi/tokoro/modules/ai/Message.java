package com.tokorokoshi.tokoro.modules.ai;

import org.springframework.ai.chat.messages.MessageType;

public record Message(MessageType role, String content) {
    public static Message systemMessage(String content) {
        return new Message(MessageType.SYSTEM, content);
    }

    public static Message userMessage(String content) {
        return new Message(MessageType.USER, content);
    }

    public static Message assistantMessage(String content) {
        return new Message(MessageType.ASSISTANT, content);
    }

    public static Message toolMessage(String content) {
        return new Message(MessageType.TOOL, content);
    }
}
