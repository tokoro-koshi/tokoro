package com.tokorokoshi.tokoro.modules.users.favorites.prompts;

import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.users.favorites.prompts.dto.CreateUpdateFavoritePromptDto;
import com.tokorokoshi.tokoro.modules.users.favorites.prompts.dto.FavoritePromptDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * REST controller for managing favorite prompts for users.
 * This controller provides endpoints to add, update, remove, and retrieve favorite prompts.
 */
@Tag(
        name = "Favorite Prompts",
        description = "API for managing favorite prompts for users"
)
@RestController
@RequestMapping("/users/favorite-prompts")
public class FavoritePromptsController {
    private final FavoritePromptsService favoritePromptsService;

    @Autowired
    public FavoritePromptsController(FavoritePromptsService favoritePromptsService) {
        this.favoritePromptsService = favoritePromptsService;
    }

    /**
     * Adds a favorite prompt to the currently authenticated user's metadata.
     *
     * @param createUpdateFavoritePromptDto the favorite prompt to add.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Add a favorite prompt",
            description = "Accepts a request with a JSON body to add a favorite prompt"
    )
    @PostMapping
    public ResponseEntity<String> addFavoritePrompt(
            @Parameter(
                    description = "The favorite prompt to add",
                    required = true
            )
            @Valid
            @RequestBody
            CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto
    ) {
        try {
            favoritePromptsService.addFavoritePrompt(
                    createUpdateFavoritePromptDto);
            return ResponseEntity.ok("Favorite prompt added successfully");
        } catch (Exception e) {
            throw new ServerErrorException("Failed to add favorite prompt", e);
        }
    }

    /**
     * Updates a favorite prompt in the currently authenticated user's metadata.
     *
     * @param promptId                      the prompt ID of the favorite prompt to update.
     * @param createUpdateFavoritePromptDto the updated favorite prompt.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Update a favorite prompt",
            description = "Accepts a request with a JSON body to update a favorite prompt"
    )
    @PutMapping("/{promptId}")
    public ResponseEntity<String> updateFavoritePrompt(
            @Parameter(
                    description = "The ID of the favorite prompt to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String promptId,
            @Parameter(
                    description = "The updated favorite prompt",
                    required = true
            )
            @Valid
            @RequestBody
            CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto
    ) {
        try {
            favoritePromptsService.updateFavoritePrompt(
                    promptId,
                    createUpdateFavoritePromptDto
            );
            return ResponseEntity.ok(
                    "Favorite prompt updated successfully"
            );
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update favorite prompt",
                    ex
            );
        }
    }

    /**
     * Removes a favorite prompt from the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to remove.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Remove a favorite prompt",
            description = "Accepts a request with a path variable to remove a favorite prompt"
    )
    @DeleteMapping("/{promptId}")
    public ResponseEntity<String> removeFavoritePrompt(
            @Parameter(
                    description = "The ID of the favorite prompt to remove",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String promptId
    ) {
        try {
            favoritePromptsService.removeFavoritePrompt(promptId);
            return ResponseEntity.ok("Favorite prompt removed successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to remove favorite prompt",
                    ex
            );
        }
    }

    /**
     * Retrieves all favorite prompts for the currently authenticated user.
     *
     * @return a response entity containing a list of {@link FavoritePromptDto} objects.
     */
    @Operation(
            summary = "Get all favorite prompts",
            description = "Returns a list of all favorite prompts"
    )
    @GetMapping
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePrompts() {
        try {
            List<FavoritePromptDto> favoritePrompts =
                    favoritePromptsService.getFavoritePrompts();
            return ResponseEntity.ok(favoritePrompts);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve favorite prompts",
                    ex
            );
        }
    }

    /**
     * Retrieves a specific favorite prompt by its prompt ID.
     *
     * @param promptId the prompt ID of the favorite prompt to retrieve.
     * @return a response entity containing the {@link FavoritePromptDto} object if found, otherwise a not found response.
     */
    @Operation(
            summary = "Get a favorite prompt by ID",
            description = "Returns the favorite prompt with the given ID"
    )
    @GetMapping("/{promptId}")
    public ResponseEntity<FavoritePromptDto> getFavoritePromptById(
            @Parameter(
                    description = "The ID of the favorite prompt to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String promptId
    ) {
        try {
            FavoritePromptDto favoritePrompt =
                    favoritePromptsService.getFavoritePromptById(promptId);
            if (favoritePrompt != null) {
                return ResponseEntity.ok(favoritePrompt);
            } else {
                throw new NotFoundException("Favorite prompt not found");
            }
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve favorite prompt by ID",
                    ex
            );
        }
    }

    /**
     * Clears all favorite prompts for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Clear all favorite prompts",
            description = "Clears all favorite prompts"
    )
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePrompts() {
        try {
            favoritePromptsService.clearFavoritePrompts();
            return ResponseEntity.ok("All favorite prompts cleared successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to clear favorite prompts",
                    ex
            );
        }
    }

    /**
     * Checks if a prompt is already a favorite for the currently authenticated user.
     *
     * @param promptId the prompt ID to check.
     * @return a response entity indicating whether the prompt is a favorite.
     */
    @Operation(
            summary = "Check if a prompt is a favorite",
            description = "Checks if the prompt with the given ID is a favorite"
    )
    @GetMapping("/check/{promptId}")
    public ResponseEntity<Boolean> isFavoritePrompt(
            @Parameter(
                    description = "The ID of the prompt to check",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String promptId
    ) {
        try {
            boolean isFavorite =
                    favoritePromptsService.isFavoritePrompt(promptId);
            return ResponseEntity.ok(isFavorite);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if prompt is a favorite",
                    ex
            );
        }
    }

    /**
     * Sorts favorite prompts by the added date in ascending order.
     *
     * @return a response entity containing a sorted list of {@link FavoritePromptDto} objects.
     */
    @Operation(
            summary = "Sort favorite prompts by date",
            description = "Returns a list of favorite prompts sorted by date"
    )
    @GetMapping("/sort/date")
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePromptsSortedByDate() {
        try {
            List<FavoritePromptDto> favoritePrompts =
                    favoritePromptsService.getFavoritePrompts();
            var comparer = Comparator.comparing(FavoritePromptDto::addedAt);
            List<FavoritePromptDto> sortedPrompts
                    = favoritePrompts.stream()
                    .sorted(comparer)
                    .toList();
            return ResponseEntity.ok(sortedPrompts);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to sort favorite prompts by date",
                    ex
            );
        }
    }

    /**
     * Sorts favorite prompts by the added date in descending order.
     *
     * @return a response entity containing a sorted list of {@link FavoritePromptDto} objects.
     */
    @Operation(
            summary = "Sort favorite prompts by date in descending order",
            description = "Returns a list of favorite prompts sorted by date in descending order"
    )
    @GetMapping("/sort/date-desc")
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePromptsSortedByDateDescending() {
        try {
            List<FavoritePromptDto> favoritePrompts =
                    favoritePromptsService.getFavoritePrompts();
            var comparer = Comparator.comparing(FavoritePromptDto::addedAt)
                    .reversed();
            List<FavoritePromptDto> sortedPrompts
                    = favoritePrompts.stream()
                    .sorted(comparer)
                    .toList();
            return ResponseEntity.ok(sortedPrompts);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to sort favorite prompts by date in descending order",
                    ex
            );
        }
    }

    /**
     * Searches favorite prompts by content.
     *
     * @param content the content to search for.
     * @return a response entity containing a list of {@link FavoritePromptDto} objects that match the search criteria.
     */
    @Operation(
            summary = "Search favorite prompts by content",
            description = "Returns a list of favorite prompts that contain the given content"
    )
    @GetMapping("/search")
    public ResponseEntity<List<FavoritePromptDto>> searchFavoritePromptsByContent(
            @Parameter(
                    description = "The content to search for",
                    required = true,
                    example = "lorem ipsum"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String content
    ) {
        try {
            List<FavoritePromptDto> favoritePrompts =
                    favoritePromptsService.getFavoritePrompts();
            Predicate<FavoritePromptDto> filter = (FavoritePromptDto fp) ->
                    fp.content()
                            .toLowerCase()
                            .contains(content.toLowerCase());
            List<FavoritePromptDto> filteredPrompts
                    = favoritePrompts.stream()
                    .filter(filter)
                    .toList();
            return ResponseEntity.ok(filteredPrompts);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to search favorite prompts by content",
                    ex
            );
        }
    }
}