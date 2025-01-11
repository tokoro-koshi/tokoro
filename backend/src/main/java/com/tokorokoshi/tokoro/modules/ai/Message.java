package com.tokorokoshi.tokoro.modules.ai;

import org.springframework.ai.chat.messages.MessageType;

public record Message(MessageType role, String content) {}
