package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.database.Conversation;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.ConversationDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateConversationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    private final MongoTemplate repository;
    private final ConversationMapper conversationMapper;
    private final ChatHistoryMapper chatHistoryMapper;

    @Autowired
    public ChatHistoryService(
            MongoTemplate repository,
            ConversationMapper conversationMapper,
            ChatHistoryMapper chatHistoryMapper
    ) {
        this.repository = repository;
        this.conversationMapper = conversationMapper;
        this.chatHistoryMapper = chatHistoryMapper;
    }

    /**
     * Saves a new chat history.
     *
     * @param userId                     user ID associated with chat history
     * @param createUpdateChatHistoryDto chat history data
     * @return the saved chat history
     */
    public ChatHistoryDto saveChatHistory(String userId, CreateUpdateChatHistoryDto createUpdateChatHistoryDto) {
        ChatHistory chatHistory = chatHistoryMapper.toChatHistoryScheme(createUpdateChatHistoryDto);
        ChatHistory savedChatHistory = repository.save(chatHistory.withUserId(userId));

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
        ChatHistory chatHistory = findChatHistoryById(id);

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
                .toList();

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
        ChatHistory existingChatHistory = findChatHistoryById(chatHistoryId);

        ChatHistory chatHistory = chatHistoryMapper.toChatHistoryScheme(createUpdateChatHistoryDto)
                .withId(existingChatHistory.id())
                .withUserId(existingChatHistory.userId())
                .withConversations(existingChatHistory.conversationIds())
                .withCreatedAt(existingChatHistory.createdAt());
        ChatHistory savedChatHistory = repository.save(chatHistory);

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Adds a new conversation to an existing chat history.
     *
     * @param chatHistoryId               chat history ID
     * @param createUpdateConversationDto conversation data to add
     * @return the updated chat history
     */
    public ChatHistoryDto addConversationToChatHistory(String chatHistoryId, CreateUpdateConversationDto createUpdateConversationDto) {
        ChatHistory existingChatHistory = findChatHistoryById(chatHistoryId);

        // Convert DTO to Conversation entity and save it
        Conversation conversation = conversationMapper.toConversationScheme(createUpdateConversationDto);
        Conversation savedConversation = repository.save(conversation);

        // Add the new conversation ID to the existing list of conversation IDs
        List<String> updatedConversationIds = existingChatHistory.conversationIds();
        updatedConversationIds.add(savedConversation.id());

        // Update and save the chat history with the new conversation
        ChatHistory savedChatHistory = repository.save(existingChatHistory.withConversations(updatedConversationIds));

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Adds new places IDs to an existing conversation in a chat history.
     *
     * @param chatHistoryId  chat history ID
     * @param conversationId conversation ID
     * @param placesIds      list of places IDs to add
     * @return the updated conversation
     */
    public ConversationDto addPlacesIdsToConversation(String chatHistoryId, String conversationId, List<String> placesIds) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Retrieve the conversation and ensure it belongs to the specified chat history
        Conversation existingConversation = repository.findById(conversationId, Conversation.class);
        if (existingConversation == null || !chatHistory.conversationIds().contains(conversationId)) {
            throw new IllegalArgumentException("Conversation not found for id: " + conversationId);
        }

        // Update the conversation with the new list of place IDs and save it
        Conversation conversation = existingConversation.withPlacesIds(placesIds);
        Conversation savedConversation = repository.save(conversation);

        return conversationMapper.toConversationDto(savedConversation);
    }

    /**
     * Updates an existing conversation in a chat history.
     *
     * @param chatHistoryId               chat history ID
     * @param conversationId              conversation ID
     * @param createUpdateConversationDto conversation data to update
     * @return the updated conversation
     */
    public ConversationDto updateConversationInChatHistory(String chatHistoryId, String conversationId, CreateUpdateConversationDto createUpdateConversationDto) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Retrieve the conversation and ensure it belongs to the specified chat history
        Conversation existingConversation = repository.findById(conversationId, Conversation.class);
        if (existingConversation == null || !chatHistory.conversationIds().contains(conversationId)) {
            throw new IllegalArgumentException("Conversation not found for id: " + conversationId);
        }

        Conversation conversation = conversationMapper.toConversationScheme(createUpdateConversationDto)
                .withId(existingConversation.id());
        Conversation savedConversation = repository.save(conversation);

        return conversationMapper.toConversationDto(savedConversation);
    }

    /**
     * Deletes a conversation from a chat history.
     *
     * @param chatHistoryId  chat history ID
     * @param conversationId conversation ID
     */
    public void deleteConversationFromChatHistory(String chatHistoryId, String conversationId) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Retrieve the conversation and ensure it belongs to the specified chat history
        Conversation existingConversation = repository.findById(conversationId, Conversation.class);
        if (existingConversation == null || !chatHistory.conversationIds().contains(conversationId)) {
            throw new IllegalArgumentException("Conversation not found for id: " + conversationId);
        }

        chatHistory.conversationIds().remove(conversationId);
        repository.save(chatHistory);

        repository.remove(existingConversation);
    }

    /**
     * Retrieves paginated conversations for a specific chat history.
     *
     * @param chatHistoryId chat history ID
     * @param pageable      pagination information (page, size)
     * @return paginated list of conversations
     */
    public Page<ConversationDto> getConversationsForChatHistory(String chatHistoryId, Pageable pageable) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Create a query to fetch conversations linked to the chat history with pagination
        Query query = Query.query(Criteria.where("id").in(chatHistory.conversationIds())).with(pageable);
        List<Conversation> conversations = repository.find(query, Conversation.class);

        // Count the total number of conversations associated with the chat history
        long total = repository.count(Query.query(Criteria.where("id").in(chatHistory.conversationIds())), Conversation.class);

        // Convert conversations to DTOs
        List<ConversationDto> content = conversations.stream()
                .map(conversationMapper::toConversationDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Finds a chat history by its ID.
     *
     * @param id the ID of the chat history to retrieve
     * @return the ChatHistory object corresponding to the given ID
     * @throws IllegalArgumentException if no chat history is found for the specified ID
     */
    private ChatHistory findChatHistoryById(String id) {
        ChatHistory chatHistory = repository.findById(id, ChatHistory.class);
        if (chatHistory == null) {
            throw new IllegalArgumentException("Chat history not found for id: " + id);
        }
        return chatHistory;
    }
}
