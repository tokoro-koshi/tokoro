package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.modules.chats.dto.ConversationDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateConversationDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Chat Histories", description = "API for managing chat histories")
@RestController
@RequestMapping("/users/{userId}/chat-histories")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;
    private final PagedResourcesAssembler<ChatHistoryDto> chatHistoryPagedResourcesAssembler;
    private final PagedResourcesAssembler<ConversationDto> conversationPagedResourcesAssembler;
    private final Logger logger = Logger.getLogger(ChatHistoryController.class.getName());

    public ChatHistoryController(
            ChatHistoryService chatHistoryService,
            PagedResourcesAssembler<ChatHistoryDto> chatHistoryPagedResourcesAssembler,
            PagedResourcesAssembler<ConversationDto> conversationPagedResourcesAssembler
    ) {
        this.chatHistoryService = chatHistoryService;
        this.chatHistoryPagedResourcesAssembler = chatHistoryPagedResourcesAssembler;
        this.conversationPagedResourcesAssembler = conversationPagedResourcesAssembler;
    }

    @Operation(
            summary = "Save a new chat history for a user",
            description = "Accepts a request with JSON data to save a new chat history for a user, and returns the saved chat history"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ChatHistoryDto> saveChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(description = "The chat history to save", required = true)
            @RequestBody
            CreateUpdateChatHistoryDto chatHistoryDto
    ) {
        return ResponseEntity.ok(this.chatHistoryService.saveChatHistory(userId, chatHistoryDto));
    }

    @Operation(
            summary = "Get a chat history by ID for a user",
            description = "Returns the chat history with the given ID for a user"
    )
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatHistoryDto> getChatHistoryById(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        var chatHistory = this.chatHistoryService.getChatHistoryById(id);
        if (chatHistory == null || !userId.equals(chatHistory.userId())) {
            throw new NotFoundException("Chat history not found");
        }
        return ResponseEntity.ok(chatHistory);
    }

    @Operation(
            summary = "Get all chat histories for a user",
            description = "Returns a paginated list of all chat histories for a user"
    )
    @GetMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<ChatHistoryDto>> getAllChatHistories(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var chatHistories = this.chatHistoryService.getAllUserChatHistories(userId, pageable);
        var pagination = PaginationDto.fromEntityModel(
                this.chatHistoryPagedResourcesAssembler.toModel(chatHistories)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Update a chat history for a user",
            description = "Accepts a request with JSON data to update a chat history for a user, and returns the updated chat history"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ChatHistoryDto> updateChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The chat history to update", required = true)
            @RequestBody
            CreateUpdateChatHistoryDto chatHistoryDto
    ) {
        ChatHistoryDto updatedChatHistory = this.chatHistoryService.updateChatHistory(id, chatHistoryDto);
        if (updatedChatHistory == null || !userId.equals(updatedChatHistory.userId())) {
            throw new NotFoundException("Chat history not found");
        }
        return ResponseEntity.ok(updatedChatHistory);
    }

    @Operation(
            summary = "Delete a chat history for a user",
            description = "Deletes the chat history with the given ID for a user"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        try {
            ChatHistoryDto chatHistory = this.chatHistoryService.getChatHistoryById(id);
            if (chatHistory == null || !userId.equals(chatHistory.userId())) {
                throw new NotFoundException("Chat history not found");
            }
            this.chatHistoryService.deleteChatHistory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Chat history not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Add a new conversation to an existing chat history for a user",
            description = "Adds a new conversation to the chat history with the given ID for a user"
    )
    @PostMapping(
            value = "/{id}/conversations",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ChatHistoryDto> addConversationToChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The conversation to add", required = true)
            @RequestBody
            CreateUpdateConversationDto conversationDto
    ) {
        ChatHistoryDto chatHistory = this.chatHistoryService.getChatHistoryById(id);
        if (chatHistory == null || !userId.equals(chatHistory.userId())) {
            throw new NotFoundException("Chat history not found");
        }
        return ResponseEntity.ok(chatHistoryService.addConversationToChatHistory(id, conversationDto));
    }

    @Operation(
            summary = "Update an existing conversation in a chat history for a user",
            description = "Updates the conversation with the specified ID in the chat history with the given ID for a user"
    )
    @PutMapping(
            value = "/{id}/conversations/{conversationId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConversationDto> updateConversationInChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The ID of the conversation to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b4"
            )
            @PathVariable
            String conversationId,
            @Parameter(description = "The conversation to update", required = true)
            @RequestBody
            CreateUpdateConversationDto conversationDto
    ) {
        ChatHistoryDto chatHistory = this.chatHistoryService.getChatHistoryById(id);
        if (chatHistory == null || !userId.equals(chatHistory.userId())) {
            throw new NotFoundException("Chat history not found");
        }
        return ResponseEntity.ok(chatHistoryService.updateConversationInChatHistory(id, conversationId, conversationDto));
    }

    @Operation(
            summary = "Delete a conversation from a chat history for a user",
            description = "Deletes the conversation with the specified ID from the chat history with the given ID for a user"
    )
    @DeleteMapping(
            value = "/{id}/conversations/{conversationId}"
    )
    public ResponseEntity<ChatHistoryDto> deleteConversationFromChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The ID of the conversation to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b4"
            )
            @PathVariable
            String conversationId
    ) {
        try {
            ChatHistoryDto chatHistory = this.chatHistoryService.getChatHistoryById(id);
            if (chatHistory == null || !userId.equals(chatHistory.userId())) {
                throw new NotFoundException("Chat history not found");
            }
            chatHistoryService.deleteConversationFromChatHistory(id, conversationId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Conversation not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get conversations for a specific chat history for a user",
            description = "Returns a paginated list of conversations for a specific chat history for a user"
    )
    @GetMapping(
            value = "/{id}/conversations",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PaginationDto<ConversationDto>> getConversationsForChatHistory(
            @Parameter(description = "The ID of the user", required = true)
            @PathVariable
            String userId,
            @Parameter(
                    description = "The ID of the chat history",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        ChatHistoryDto chatHistory = this.chatHistoryService.getChatHistoryById(id);
        if (chatHistory == null || !userId.equals(chatHistory.userId())) {
            throw new NotFoundException("Chat history not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        var conversations = chatHistoryService.getConversationsForChatHistory(id, pageable);
        var pagination = PaginationDto.fromEntityModel(conversationPagedResourcesAssembler.toModel(conversations));
        return ResponseEntity.ok(pagination);
    }
}
