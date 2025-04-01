package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.database.Message;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryService {

    private final MongoTemplate repository;
    private final ChatHistoryMapper chatHistoryMapper;

    @Autowired
    public ChatHistoryService(
            MongoTemplate repository,
            ChatHistoryMapper chatHistoryMapper
    ) {
        this.repository = repository;
        this.chatHistoryMapper = chatHistoryMapper;
    }

    /**
     * Saves a new chat history.
     *
     * @param createUpdateChatHistoryDto chat history data
     * @return the saved chat history
     */
    public ChatHistoryDto saveChatHistory(CreateUpdateChatHistoryDto createUpdateChatHistoryDto) {
        ChatHistory chatHistory = chatHistoryMapper.toChatHistoryScheme(createUpdateChatHistoryDto);
        ChatHistory savedChatHistory = repository.save(chatHistory);

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Retrieves a chat history by ID.
     *
     * @param id chat history ID
     * @return the chat history
     */
    public ChatHistoryDto getChatHistoryById(String id) {
        ChatHistory chatHistory = repository.findById(id, ChatHistory.class);
        if (chatHistory == null) return null;

        return chatHistoryMapper.toChatHistoryDto(chatHistory);
    }

    /**
     * Deletes a chat history by ID.
     *
     * @param id chat history ID
     */
    public void deleteChatHistory(String id) {
        ChatHistory chatHistory = repository.findById(id, ChatHistory.class);
        if (chatHistory == null) {
            throw new IllegalArgumentException("Chat history not found for id: " + id);
        }

        repository.remove(chatHistory);
    }

    /**
     * Retrieves chat histories for a specific user.
     *
     * @param userId   user ID
     * @param pageable pagination information (page, size)
     * @return paginated list of chat histories for the user
     */
    public Page<ChatHistoryDto> getAllUserChatHistories(String userId, Pageable pageable) {
        // Create a query with pagination information and filter by user ID
        Query query = Query.query(Criteria.where("userId").is(userId)).with(pageable);

        List<ChatHistory> chatHistories = repository.find(query, ChatHistory.class);

        // Count the total number of chat histories for the user
        long total = repository.count(Query.query(Criteria.where("userId").is(userId)), ChatHistory.class);

        List<ChatHistoryDto> content = chatHistories.stream()
                .map(chatHistoryMapper::toChatHistoryDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Updates an existing chat history.
     *
     * @param chatHistoryId                         chat history ID
     * @param createUpdateChatHistoryDto chat history data to update
     * @return the updated chat history
     */
    public ChatHistoryDto updateChatHistory(String chatHistoryId, CreateUpdateChatHistoryDto createUpdateChatHistoryDto) {
        ChatHistory existingChatHistory = repository.findById(chatHistoryId, ChatHistory.class);
        if (existingChatHistory == null) {
            throw new IllegalArgumentException("Chat history not found for id: " + chatHistoryId);
        }

        ChatHistory chatHistory = chatHistoryMapper.toChatHistoryScheme(createUpdateChatHistoryDto)
                .withId(existingChatHistory.id())
                .withTitle(existingChatHistory.title())
                .withCreatedAt(existingChatHistory.createdAt());
        ChatHistory savedChatHistory = repository.save(chatHistory);

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Adds messages to chat history, creating a new one if conversationId is null/blank
     *
     * @param conversationId optional conversation ID
     * @param userId user ID
     * @param title chat title/prompt
     * @param messages messages to add
     * @return the created or updated chat history
     */
    public ChatHistoryDto addToChatHistory(String conversationId, String userId, String title, List<Message> messages) {
        ChatHistoryDto chatHistoryDto;

        if (conversationId == null || conversationId.isBlank()) {
            CreateUpdateChatHistoryDto newChatDto = new CreateUpdateChatHistoryDto(
                    title,
                    userId,
                    new ArrayList<>(messages)
            );
            chatHistoryDto = saveChatHistory(newChatDto);
        } else {
            ChatHistoryDto existingChat = getChatHistoryById(conversationId);
            if (existingChat == null) {
                throw new IllegalArgumentException("Chat history not found for id: " + conversationId);
            }

            List<Message> updatedMessages = new ArrayList<>(existingChat.messages());
            updatedMessages.addAll(messages);

            CreateUpdateChatHistoryDto updateChatDto = new CreateUpdateChatHistoryDto(
                    userId,
                    updatedMessages
            );
            chatHistoryDto = updateChatHistory(conversationId, updateChatDto);
        }

        return chatHistoryDto;
    }
}
