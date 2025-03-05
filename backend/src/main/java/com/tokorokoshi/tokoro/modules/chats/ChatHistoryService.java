package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.database.ChatHistory;
import com.tokorokoshi.tokoro.database.Message;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateMessageDto;
import com.tokorokoshi.tokoro.modules.chats.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryService {

    private final MongoTemplate repository;
    private final ChatHistoryMapper chatHistoryMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public ChatHistoryService(
            MongoTemplate repository,
            ChatHistoryMapper chatHistoryMapper,
            MessageMapper messageMapper
    ) {
        this.repository = repository;
        this.chatHistoryMapper = chatHistoryMapper;
        this.messageMapper = messageMapper;
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
        ChatHistory existingChatHistory = findChatHistoryById(chatHistoryId);

        ChatHistory chatHistory = chatHistoryMapper.toChatHistoryScheme(createUpdateChatHistoryDto)
                .withId(existingChatHistory.id())
                .withMessagesIds(existingChatHistory.messagesIds())
                .withCreatedAt(existingChatHistory.createdAt());
        ChatHistory savedChatHistory = repository.save(chatHistory);

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Adds a new message to an existing chat history.
     *
     * @param chatHistoryId               chat history ID
     * @param createUpdateMessageDto message data to add
     * @return the updated chat history
     */
    public ChatHistoryDto addMessageToChatHistory(String chatHistoryId, CreateUpdateMessageDto createUpdateMessageDto) {
        ChatHistory existingChatHistory = findChatHistoryById(chatHistoryId);

        // Convert DTO to Message entity and save it
        Message message = messageMapper.toMessageScheme(createUpdateMessageDto);
        Message savedMessage = repository.save(message);

        // Add the new message ID to the existing list of message IDs
        List<String> updatedMessageIds = existingChatHistory.messagesIds();
        updatedMessageIds.add(savedMessage.id());

        // Update and save the chat history with the new message
        ChatHistory savedChatHistory = repository.save(existingChatHistory.withMessagesIds(updatedMessageIds));

        return chatHistoryMapper.toChatHistoryDto(savedChatHistory);
    }

    /**
     * Updates an existing message in a chat history.
     *
     * @param chatHistoryId               chat history ID
     * @param messageId                   message ID
     * @param createUpdateMessageDto message data to update
     * @return the updated message
     */
    public MessageDto updateMessageInChatHistory(String chatHistoryId, String messageId, CreateUpdateMessageDto createUpdateMessageDto) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Retrieve the message and ensure it belongs to the specified chat history
        Message existingMessage = repository.findById(messageId, Message.class);
        if (existingMessage == null || !chatHistory.messagesIds().contains(messageId)) {
            throw new IllegalArgumentException("Message not found for id: " + messageId);
        }

        Message message = messageMapper.toMessageScheme(createUpdateMessageDto)
                .withId(existingMessage.id());
        Message savedMessage = repository.save(message);

        return messageMapper.toMessageDto(savedMessage);
    }

    /**
     * Deletes a message from a chat history.
     *
     * @param chatHistoryId  chat history ID
     * @param messageId message ID
     */
    public void deleteMessageFromChatHistory(String chatHistoryId, String messageId) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Retrieve the message and ensure it belongs to the specified chat history
        Message existingMessage = repository.findById(messageId, Message.class);
        if (existingMessage == null || !chatHistory.messagesIds().contains(messageId)) {
            throw new IllegalArgumentException("Message not found for id: " + messageId);
        }

        chatHistory.messagesIds().remove(messageId);
        repository.save(chatHistory);

        repository.remove(existingMessage);
    }

    /**
     * Retrieves paginated messages for a specific chat history.
     *
     * @param chatHistoryId chat history ID
     * @param pageable      pagination information (page, size)
     * @return paginated list of messages
     */
    public Page<MessageDto> getMessagesForChatHistory(String chatHistoryId, Pageable pageable) {
        ChatHistory chatHistory = findChatHistoryById(chatHistoryId);

        // Create a query to fetch messages linked to the chat history with pagination
        Query query = Query.query(Criteria.where("id").in(chatHistory.messagesIds())).with(pageable);
        List<Message> messages = repository.find(query, Message.class);

        // Count the total number of messages associated with the chat history
        long total = repository.count(Query.query(Criteria.where("id").in(chatHistory.messagesIds())), Message.class);

        // Convert messages to DTOs
        List<MessageDto> content = messages.stream()
                .map(messageMapper::toMessageDto)
                .collect(Collectors.toList());

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
