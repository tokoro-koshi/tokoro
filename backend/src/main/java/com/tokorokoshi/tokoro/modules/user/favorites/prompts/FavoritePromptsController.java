package com.tokorokoshi.tokoro.modules.user.favorites.prompts;

import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.CreateUpdateFavoritePromptDto;
import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.FavoritePromptDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing favorite prompts for users.
 * This controller provides endpoints to add, update, remove, and retrieve favorite prompts.
 */
@RestController
@RequestMapping("/api/v1/users/favorite-prompts")
public class FavoritePromptsController {

    private static final Logger log = LoggerFactory.getLogger(FavoritePromptsController.class);

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
    @PostMapping
    public ResponseEntity<String> addFavoritePrompt(@Valid @RequestBody CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) {
        try {
            favoritePromptsService.addFavoritePrompt(createUpdateFavoritePromptDto);
            return ResponseEntity.ok("Favorite prompt added successfully");
        } catch (Exception e) {
            log.error("Error adding favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add favorite prompt");
        }
    }

    /**
     * Updates a favorite prompt in the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to update.
     * @param createUpdateFavoritePromptDto the updated favorite prompt.
     * @return a response entity indicating success or failure.
     */
    @PutMapping("/{promptId}")
    public ResponseEntity<String> updateFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId,
            @Valid @RequestBody CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) {
        try {
            favoritePromptsService.updateFavoritePrompt(promptId, createUpdateFavoritePromptDto);
            return ResponseEntity.ok("Favorite prompt updated successfully");
        } catch (Exception e) {
            log.error("Error updating favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update favorite prompt");
        }
    }

    /**
     * Removes a favorite prompt from the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to remove.
     * @return a response entity indicating success or failure.
     */
    @DeleteMapping("/{promptId}")
    public ResponseEntity<String> removeFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            favoritePromptsService.removeFavoritePrompt(promptId);
            return ResponseEntity.ok("Favorite prompt removed successfully");
        } catch (Exception e) {
            log.error("Error removing favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove favorite prompt");
        }
    }

    /**
     * Retrieves all favorite prompts for the currently authenticated user.
     *
     * @return a response entity containing a list of {@link FavoritePromptDto} objects.
     */
    @GetMapping
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePrompts() {
        try {
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            return ResponseEntity.ok(favoritePrompts);
        } catch (Exception e) {
            log.error("Error retrieving favorite prompts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific favorite prompt by its prompt ID.
     *
     * @param promptId the prompt ID of the favorite prompt to retrieve.
     * @return a response entity containing the {@link FavoritePromptDto} object if found, otherwise a not found response.
     */
    @GetMapping("/{promptId}")
    public ResponseEntity<FavoritePromptDto> getFavoritePromptById(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            FavoritePromptDto favoritePrompt = favoritePromptsService.getFavoritePromptById(promptId);
            if (favoritePrompt != null) {
                return ResponseEntity.ok(favoritePrompt);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Error retrieving favorite prompt by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Clears all favorite prompts for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePrompts() {
        try {
            favoritePromptsService.clearFavoritePrompts();
            return ResponseEntity.ok("All favorite prompts cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing favorite prompts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear favorite prompts");
        }
    }

    /**
     * Checks if a prompt is already a favorite for the currently authenticated user.
     *
     * @param promptId the prompt ID to check.
     * @return a response entity indicating whether the prompt is a favorite.
     */
    @GetMapping("/check/{promptId}")
    public ResponseEntity<Boolean> isFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            boolean isFavorite = favoritePromptsService.isFavoritePrompt(promptId);
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            log.error("Error checking if prompt is a favorite", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sorts favorite prompts by the added date in ascending order.
     *
     * @return a response entity containing a sorted list of {@link FavoritePromptDto} objects.
     */
    @GetMapping("/sort/date")
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePromptsSortedByDate() {
        try {
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> sortedPrompts = favoritePrompts.stream()
                    .sorted(Comparator.comparing(FavoritePromptDto::addedAt))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(sortedPrompts);
        } catch (Exception e) {
            log.error("Error sorting favorite prompts by date", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sorts favorite prompts by the added date in descending order.
     *
     * @return a response entity containing a sorted list of {@link FavoritePromptDto} objects.
     */
    @GetMapping("/sort/date-desc")
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePromptsSortedByDateDescending() {
        try {
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> sortedPrompts = favoritePrompts.stream()
                    .sorted(Comparator.comparing(FavoritePromptDto::addedAt).reversed())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(sortedPrompts);
        } catch (Exception e) {
            log.error("Error sorting favorite prompts by date in descending order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Searches favorite prompts by content.
     *
     * @param content the content to search for.
     * @return a response entity containing a list of {@link FavoritePromptDto} objects that match the search criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<List<FavoritePromptDto>> searchFavoritePromptsByContent(
            @RequestParam @NotNull @NotBlank String content) {
        try {
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> filteredPrompts = favoritePrompts.stream()
                    .filter(fp -> fp.content().toLowerCase().contains(content.toLowerCase()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredPrompts);
        } catch (Exception e) {
            log.error("Error searching favorite prompts by content", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}