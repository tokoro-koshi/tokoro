package com.tokorokoshi.tokoro.modules.user.favorites.prompts;

import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.InvalidPromptIdException;
import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.CreateUpdateFavoritePromptDto;
import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.FavoritePromptDto;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.FavoritePromptNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.NoFavoritePromptsException;
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
        if (favoritePromptsService == null) {
            throw new IllegalArgumentException("FavoritePromptsService must not be null");
        }
        this.favoritePromptsService = favoritePromptsService;
    }

    /**
     * Adds a favorite prompt to the currently authenticated user's metadata.
     *
     * @param createUpdateFavoritePromptDto the favorite prompt to add.
     * @return a response entity indicating success or failure.
     * @throws InvalidPromptIdException if the DTO is invalid.
     */
    @PostMapping
    public ResponseEntity<String> addFavoritePrompt(@Valid @RequestBody CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) {
        try {
            log.debug("Adding favorite prompt: {}", createUpdateFavoritePromptDto);
            favoritePromptsService.addFavoritePrompt(createUpdateFavoritePromptDto);
            log.info("Successfully added favorite prompt");
            return ResponseEntity.ok("Favorite prompt added successfully");
        } catch (InvalidPromptIdException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error adding favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add favorite prompt: " + e.getMessage());
        }
    }

    /**
     * Updates a favorite prompt in the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to update.
     * @param createUpdateFavoritePromptDto the updated favorite prompt.
     * @return a response entity indicating success or failure.
     * @throws InvalidPromptIdException if the prompt ID or DTO is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    @PutMapping("/{promptId}")
    public ResponseEntity<String> updateFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId,
            @Valid @RequestBody CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) {
        try {
            log.debug("Updating favorite prompt with prompt ID: {}", promptId);
            favoritePromptsService.updateFavoritePrompt(promptId, createUpdateFavoritePromptDto);
            log.info("Successfully updated favorite prompt with prompt ID: {}", promptId);
            return ResponseEntity.ok("Favorite prompt updated successfully");
        } catch (InvalidPromptIdException e) {
            log.error("Invalid prompt ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid prompt ID: " + e.getMessage());
        } catch (FavoritePromptNotFoundException e) {
            log.warn("Favorite prompt with prompt ID {} not found", promptId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Favorite prompt with prompt ID " + promptId + " not found");
        } catch (Exception e) {
            log.error("Error updating favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update favorite prompt: " + e.getMessage());
        }
    }

    /**
     * Removes a favorite prompt from the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to remove.
     * @return a response entity indicating success or failure.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    @DeleteMapping("/{promptId}")
    public ResponseEntity<String> removeFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            log.debug("Removing favorite prompt with prompt ID: {}", promptId);
            favoritePromptsService.removeFavoritePrompt(promptId);
            log.info("Successfully removed favorite prompt with prompt ID: {}", promptId);
            return ResponseEntity.ok("Favorite prompt removed successfully");
        } catch (InvalidPromptIdException e) {
            log.error("Invalid prompt ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid prompt ID: " + e.getMessage());
        } catch (FavoritePromptNotFoundException e) {
            log.warn("Favorite prompt with prompt ID {} not found", promptId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Favorite prompt with prompt ID " + promptId + " not found");
        } catch (Exception e) {
            log.error("Error removing favorite prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove favorite prompt: " + e.getMessage());
        }
    }

    /**
     * Retrieves all favorite prompts for the currently authenticated user.
     *
     * @return a response entity containing a list of {@link FavoritePromptDto} objects.
     * @throws NoFavoritePromptsException if there are no favorite prompts.
     */
    @GetMapping
    public ResponseEntity<List<FavoritePromptDto>> getFavoritePrompts() {
        try {
            log.debug("Retrieving all favorite prompts");
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            log.info("Successfully retrieved {} favorite prompts", favoritePrompts.size());
            return ResponseEntity.ok(favoritePrompts);
        } catch (NoFavoritePromptsException e) {
            log.warn("No favorite prompts found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error retrieving favorite prompts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves a specific favorite prompt by its prompt ID.
     *
     * @param promptId the prompt ID of the favorite prompt to retrieve.
     * @return a response entity containing the {@link FavoritePromptDto} object if found, otherwise a not found response.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    @GetMapping("/{promptId}")
    public ResponseEntity<FavoritePromptDto> getFavoritePromptById(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            log.debug("Retrieving favorite prompt with prompt ID: {}", promptId);
            FavoritePromptDto favoritePrompt = favoritePromptsService.getFavoritePromptById(promptId);
            if (favoritePrompt != null) {
                log.info("Successfully retrieved favorite prompt with prompt ID: {}", promptId);
                return ResponseEntity.ok(favoritePrompt);
            } else {
                log.warn("Favorite prompt with prompt ID {} not found", promptId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return null for not found
            }
        } catch (InvalidPromptIdException e) {
            log.error("Invalid prompt ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null); // Return null for bad request
        } catch (FavoritePromptNotFoundException e) {
            log.warn("Favorite prompt with prompt ID {} not found", promptId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return null for not found
        } catch (Exception e) {
            log.error("Error retrieving favorite prompt by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Clears all favorite prompts for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     * @throws NoFavoritePromptsException if there are no favorite prompts to clear.
     */
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePrompts() {
        try {
            log.debug("Clearing all favorite prompts");
            favoritePromptsService.clearFavoritePrompts();
            log.info("Successfully cleared all favorite prompts");
            return ResponseEntity.ok("All favorite prompts cleared successfully");
        } catch (NoFavoritePromptsException e) {
            log.warn("No favorite prompts to clear");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No favorite prompts to clear");
        } catch (Exception e) {
            log.error("Error clearing favorite prompts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear favorite prompts: " + e.getMessage());
        }
    }

    /**
     * Checks if a prompt is already a favorite for the currently authenticated user.
     *
     * @param promptId the prompt ID to check.
     * @return a response entity indicating whether the prompt is a favorite.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     */
    @GetMapping("/check/{promptId}")
    public ResponseEntity<Boolean> isFavoritePrompt(
            @PathVariable @NotNull @NotBlank String promptId) {
        try {
            log.debug("Checking if prompt ID {} is a favorite", promptId);
            boolean isFavorite = favoritePromptsService.isFavoritePrompt(promptId);
            log.info("Prompt ID {} is favorite: {}", promptId, isFavorite);
            return ResponseEntity.ok(isFavorite);
        } catch (InvalidPromptIdException e) {
            log.error("Invalid prompt ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body(false); // Return false for bad request
        } catch (Exception e) {
            log.error("Error checking if prompt is a favorite", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
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
            log.debug("Sorting favorite prompts by date");
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> sortedPrompts = favoritePrompts.stream()
                    .sorted(Comparator.comparing(FavoritePromptDto::addedAt))
                    .collect(Collectors.toList());
            log.info("Successfully sorted {} favorite prompts by date", sortedPrompts.size());
            return ResponseEntity.ok(sortedPrompts);
        } catch (NoFavoritePromptsException e) {
            log.warn("No favorite prompts found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error sorting favorite prompts by date", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
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
            log.debug("Sorting favorite prompts by date in descending order");
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> sortedPrompts = favoritePrompts.stream()
                    .sorted(Comparator.comparing(FavoritePromptDto::addedAt).reversed())
                    .collect(Collectors.toList());
            log.info("Successfully sorted {} favorite prompts by date in descending order", sortedPrompts.size());
            return ResponseEntity.ok(sortedPrompts);
        } catch (NoFavoritePromptsException e) {
            log.warn("No favorite prompts found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error sorting favorite prompts by date in descending order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Searches favorite prompts by content.
     *
     * @param content the content to search for.
     * @return a response entity containing a list of {@link FavoritePromptDto} objects that match the search criteria.
     * @throws InvalidPromptIdException if the content is invalid.
     */
    @GetMapping("/search")
    public ResponseEntity<List<FavoritePromptDto>> searchFavoritePromptsByContent(
            @RequestParam @NotNull @NotBlank String content) {
        try {
            log.debug("Searching favorite prompts by content: {}", content);
            List<FavoritePromptDto> favoritePrompts = favoritePromptsService.getFavoritePrompts();
            List<FavoritePromptDto> filteredPrompts = favoritePrompts.stream()
                    .filter(fp -> fp.content().toLowerCase().contains(content.toLowerCase()))
                    .collect(Collectors.toList());
            log.info("Successfully searched favorite prompts by content '{}': found {} results", content, filteredPrompts.size());
            return ResponseEntity.ok(filteredPrompts);
        } catch (InvalidPromptIdException e) {
            log.error("Invalid content: {}", e.getMessage());
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list for bad request
        } catch (Exception e) {
            log.error("Error searching favorite prompts by content", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list for internal server error
        }
    }
}