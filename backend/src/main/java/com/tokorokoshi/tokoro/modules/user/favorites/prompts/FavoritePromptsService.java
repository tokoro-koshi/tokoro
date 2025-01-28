package com.tokorokoshi.tokoro.modules.user.favorites.prompts;

import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.InvalidPromptIdException;
import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.CreateUpdateFavoritePromptDto;
import com.tokorokoshi.tokoro.modules.user.favorites.prompts.dto.FavoritePromptDto;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.FavoritePromptNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.prompts.NoFavoritePromptsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing favorite prompts for users.
 * This class handles operations such as adding, updating, removing, and retrieving favorite prompts.
 */
@Service
public class FavoritePromptsService {

    private static final Logger log = LoggerFactory.getLogger(FavoritePromptsService.class);

    private static final String FAVORITE_PROMPTS_KEY = "favorite_prompts";

    private final Auth0UserDataService auth0UserDataService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FavoritePromptsService(Auth0UserDataService auth0UserDataService, ObjectMapper objectMapper) {
        if (auth0UserDataService == null) {
            throw new IllegalArgumentException("Auth0UserDataService must not be null");
        }
        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper must not be null");
        }
        this.auth0UserDataService = auth0UserDataService;
        this.objectMapper = objectMapper;
    }

    /**
     * Adds a favorite prompt to the currently authenticated user's metadata.
     *
     * @param createUpdateFavoritePromptDto the favorite prompt to add.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws IllegalArgumentException if the DTO is invalid.
     */
    public void addFavoritePrompt(@Valid CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) throws Auth0ManagementException {
        validateCreateUpdateFavoritePromptDto(createUpdateFavoritePromptDto);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);

        String promptId = UUID.randomUUID().toString();
        FavoritePromptDto favoritePromptDto = new FavoritePromptDto(
                promptId,
                createUpdateFavoritePromptDto.content(),
                createUpdateFavoritePromptDto.addedAt()
        );

        favoritePrompts.add(favoritePromptDto);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PROMPTS_KEY, favoritePrompts));
        log.info("Added favorite prompt with prompt ID {} for user {}", promptId, userId);
    }

    /**
     * Updates a favorite prompt in the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to update.
     * @param createUpdateFavoritePromptDto the updated favorite prompt.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    public void updateFavoritePrompt(@NotNull String promptId, @Valid CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) throws Auth0ManagementException {
        validatePromptId(promptId);
        validateCreateUpdateFavoritePromptDto(createUpdateFavoritePromptDto);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.stream().noneMatch(fp -> fp.promptId().equals(promptId))) {
            log.warn("Favorite prompt with prompt ID {} does not exist for user {}", promptId, userId);
            throw new FavoritePromptNotFoundException("Favorite prompt with prompt ID " + promptId + " does not exist for user " + userId);
        }

        favoritePrompts = favoritePrompts.stream()
                .map(fp -> fp.promptId().equals(promptId) ?
                        new FavoritePromptDto(promptId, createUpdateFavoritePromptDto.content(), createUpdateFavoritePromptDto.addedAt()) : fp)
                .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PROMPTS_KEY, favoritePrompts));
        log.info("Updated favorite prompt with prompt ID {} for user {}", promptId, userId);
    }

    /**
     * Removes a favorite prompt from the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to remove.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    public void removeFavoritePrompt(@NotNull String promptId) throws Auth0ManagementException {
        validatePromptId(promptId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.stream().noneMatch(fp -> fp.promptId().equals(promptId))) {
            log.warn("Favorite prompt with prompt ID {} does not exist for user {}", promptId, userId);
            throw new FavoritePromptNotFoundException("Favorite prompt with prompt ID " + promptId + " does not exist for user " + userId);
        }

        favoritePrompts = favoritePrompts.stream()
                .filter(fp -> !fp.promptId().equals(promptId))
                .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PROMPTS_KEY, favoritePrompts));
        log.info("Removed favorite prompt with prompt ID {} for user {}", promptId, userId);
    }

    /**
     * Retrieves all favorite prompts for the currently authenticated user.
     *
     * @return a list of {@link FavoritePromptDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<FavoritePromptDto> getFavoritePrompts() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        log.debug("Retrieved {} favorite prompts for user {}", favoritePrompts.size(), userId);
        return favoritePrompts;
    }

    /**
     * Retrieves a specific favorite prompt by its prompt ID.
     *
     * @param promptId the prompt ID of the favorite prompt to retrieve.
     * @return the {@link FavoritePromptDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     * @throws FavoritePromptNotFoundException if the favorite prompt with the specified prompt ID does not exist.
     */
    public FavoritePromptDto getFavoritePromptById(@NotNull String promptId) throws Auth0ManagementException {
        validatePromptId(promptId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        Optional<FavoritePromptDto> favoritePrompt = favoritePrompts.stream()
                .filter(fp -> fp.promptId().equals(promptId))
                .findFirst();

        if (favoritePrompt.isPresent()) {
            log.info("Retrieved favorite prompt with prompt ID {} for user {}", promptId, userId);
            return favoritePrompt.get();
        }

        log.warn("Favorite prompt with prompt ID {} does not exist for user {}", promptId, userId);
        throw new FavoritePromptNotFoundException("Favorite prompt with prompt ID " + promptId + " does not exist for user " + userId);
    }

    /**
     * Clears all favorite prompts for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoFavoritePromptsException if there are no favorite prompts to clear.
     */
    public void clearFavoritePrompts() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.isEmpty()) {
            log.warn("No favorite prompts to clear for user {}", userId);
            throw new NoFavoritePromptsException("No favorite prompts to clear for user " + userId);
        }

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PROMPTS_KEY, new ArrayList<>()));
        log.info("Cleared all favorite prompts for user {}", userId);
    }

    /**
     * Checks if a prompt is already a favorite for the currently authenticated user.
     *
     * @param promptId the prompt ID to check.
     * @return true if the prompt is a favorite, false otherwise.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws InvalidPromptIdException if the prompt ID is invalid.
     */
    public boolean isFavoritePrompt(@NotNull String promptId) throws Auth0ManagementException {
        validatePromptId(promptId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePromptDto> favoritePrompts = getFavoritePromptsFromMetadata(userMetadata);
        boolean isFavorite = favoritePrompts.stream().anyMatch(fp -> fp.promptId().equals(promptId));

        log.debug("Checked if prompt ID {} is a favorite for user {}: {}", promptId, userId, isFavorite);
        return isFavorite;
    }

    /**
     * Validates that the provided prompt ID is not null or empty.
     *
     * @param promptId the prompt ID to validate.
     * @throws InvalidPromptIdException if the prompt ID is null or empty.
     */
    private void validatePromptId(String promptId) {
        if (promptId == null || promptId.trim().isEmpty()) {
            log.error("Prompt ID must not be null or empty");
            throw new InvalidPromptIdException("Prompt ID must not be null or empty");
        }
    }

    /**
     * Validates that the provided CreateUpdateFavoritePromptDto is not null.
     *
     * @param createUpdateFavoritePromptDto the DTO to validate.
     * @throws IllegalArgumentException if the DTO is null.
     */
    private void validateCreateUpdateFavoritePromptDto(CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto) {
        if (createUpdateFavoritePromptDto == null) {
            log.error("CreateUpdateFavoritePromptDto must not be null");
            throw new IllegalArgumentException("CreateUpdateFavoritePromptDto must not be null");
        }
    }

    /**
     * Extracts favorite prompts from user metadata.
     *
     * @param userMetadata the user metadata map.
     * @return a list of {@link FavoritePromptDto} objects.
     */
    @SuppressWarnings("unchecked")
    private List<FavoritePromptDto> getFavoritePromptsFromMetadata(Map<String, Object> userMetadata) {
        List<FavoritePromptDto> favoritePrompts = new ArrayList<>();
        if (userMetadata.containsKey(FAVORITE_PROMPTS_KEY)) {
            List<Map<String, Object>> favoritePromptsMap = (List<Map<String, Object>>) userMetadata.get(FAVORITE_PROMPTS_KEY);
            favoritePrompts = favoritePromptsMap.stream()
                    .map(map -> objectMapper.convertValue(map, FavoritePromptDto.class))
                    .collect(Collectors.toList());
        }
        return favoritePrompts;
    }
}