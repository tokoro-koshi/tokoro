package com.tokorokoshi.tokoro.modules.chats;

import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.chats.dto.CreateUpdateChatHistoryDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
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

@Tag(name = "Chat Histories", description = "API for managing chat histories and messages")
@RestController
@RequestMapping("/chat-histories")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;
    private final PagedResourcesAssembler<ChatHistoryDto> chatHistoryPagedResourcesAssembler;
    private final Logger logger = Logger.getLogger(ChatHistoryController.class.getName());

    public ChatHistoryController(
            ChatHistoryService chatHistoryService,
            PagedResourcesAssembler<ChatHistoryDto> chatHistoryPagedResourcesAssembler
    ) {
        this.chatHistoryService = chatHistoryService;
        this.chatHistoryPagedResourcesAssembler = chatHistoryPagedResourcesAssembler;
    }

    @Operation(
            summary = "Get a chat history by ID",
            description = "Returns the chat history with the given ID"
    )
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatHistoryDto> getChatHistoryById(
            @Parameter(
                    description = "The ID of the chat history to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        var chatHistory = this.chatHistoryService.getChatHistoryById(id);
        if (chatHistory == null) {
            throw new NotFoundException("Chat history not found");
        }
        return ResponseEntity.ok(chatHistory);
    }

    @Operation(
            summary = "Get all chat histories for a user",
            description = "Returns a paginated list of all chat histories for a user"
    )
    @GetMapping(value = "/users/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<ChatHistoryDto>> getAllChatHistoriesByUser(
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
            summary = "Delete a chat history",
            description = "Deletes the chat history with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteChatHistory(
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
            if (chatHistory == null) {
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
}
