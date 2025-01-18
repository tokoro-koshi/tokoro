package com.tokorokoshi.tokoro.modules.tags;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.ai.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.MessageType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TagsPromptEnhancer {
    private static final Logger log = LoggerFactory.getLogger(TagsPromptEnhancer.class);
    private static List<String> systemMessagesKeys;
    private static List<Message> systemMessages;

    static {
        try {
            // Set system messages keys
            TagsPromptEnhancer.systemMessagesKeys = List.of(
                "description",
                "outputRequirements",
                "validInputExample",
                "validOutputExample",
                "invalidInputExample",
                "invalidOutputExample"
            );

            // Read and parse the JSON prompt
            String jsonContent = Files.readString(
                Path.of("src/main/resources/prompt.json")
            );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            // Extract system messages
            TagsPromptEnhancer.systemMessages = TagsPromptEnhancer
                .systemMessagesKeys
                .stream()
                .map(key ->
                         Message.systemMessage(rootNode.get(key).asText())
                )
                .toList();
        } catch (Exception e) {
            log.error("Failed to read prompt.json", e);
            System.exit(1);
        }
    }

    protected static List<Message> getSystemMessages() {
        return TagsPromptEnhancer.systemMessages;
    }

    public static List<Message> enhancePrompt(String prompt) {
        List<Message> enhancedList = new ArrayList<>(
            TagsPromptEnhancer.getSystemMessages()
        );
        enhancedList.add(new Message(MessageType.USER, prompt));
        return enhancedList;
    }
}
