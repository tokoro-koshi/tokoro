package com.tokorokoshi.tokoro.modules.users.favorites.prompts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.users.UserService;
import com.tokorokoshi.tokoro.modules.users.favorites.prompts.dto.CreateUpdateFavoritePromptDto;
import com.tokorokoshi.tokoro.modules.users.favorites.prompts.dto.FavoritePromptDto;
import com.tokorokoshi.tokoro.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for managing favorite prompts for users.
 * This class handles operations such as adding, updating, removing, and retrieving favorite prompts.
 */
@Service
public class FavoritePromptsService {

    private static final String FAVORITE_PROMPTS_KEY = "favorite_prompts";

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Auth0ManagementService auth0ManagementService;

    @Autowired
    public FavoritePromptsService(
            UserService userService,
            ObjectMapper objectMapper,
            Auth0ManagementService auth0ManagementService) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.auth0ManagementService = auth0ManagementService;
    }

    /**
     * Adds a favorite prompt to the currently authenticated user's metadata.
     *
     * @param createUpdateFavoritePromptDto the favorite prompt to add.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void addFavoritePrompt(
            @Valid CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto
    ) {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);

        String promptId = UUID.randomUUID().toString();
        FavoritePromptDto favoritePromptDto = new FavoritePromptDto(
                promptId,
                createUpdateFavoritePromptDto.content(),
                createUpdateFavoritePromptDto.addedAt() != null
                        ? createUpdateFavoritePromptDto.addedAt()
                        : new Date()
        );

        favoritePrompts.add(favoritePromptDto);
        auth0ManagementService.updateUserMetadata(
                SecurityUtils.getAuthenticatedUserId(),
                Map.of(
                        FAVORITE_PROMPTS_KEY,
                        favoritePrompts
                )
        );
    }

    /**
     * Updates a favorite prompt in the currently authenticated user's metadata.
     *
     * @param promptId                      the prompt ID of the favorite prompt to update.
     * @param createUpdateFavoritePromptDto the updated favorite prompt.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateFavoritePrompt(
            @NotNull String promptId,
            @Valid
            CreateUpdateFavoritePromptDto createUpdateFavoritePromptDto
    ) {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.stream()
                .noneMatch(fp -> fp.promptId().equals(promptId))) {
            return;
        }

        favoritePrompts = favoritePrompts.stream()
                .map(fp -> fp.promptId()
                        .equals(promptId) ?
                        new FavoritePromptDto(
                                promptId,
                                createUpdateFavoritePromptDto.content(),
                                createUpdateFavoritePromptDto.addedAt()
                        ) : fp)
                .toList();

        auth0ManagementService.updateUserMetadata(
                SecurityUtils.getAuthenticatedUserId(),
                Map.of(
                        FAVORITE_PROMPTS_KEY,
                        favoritePrompts
                )
        );
    }

    /**
     * Removes a favorite prompt from the currently authenticated user's metadata.
     *
     * @param promptId the prompt ID of the favorite prompt to remove.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void removeFavoritePrompt(@NotNull String promptId) {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.stream()
                .noneMatch(fp -> fp.promptId().equals(promptId))) {
            return;
        }

        favoritePrompts = favoritePrompts.stream()
                .filter(fp -> !fp.promptId()
                        .equals(promptId))
                .toList();

        auth0ManagementService.updateUserMetadata(
                SecurityUtils.getAuthenticatedUserId(),
                Map.of(
                        FAVORITE_PROMPTS_KEY,
                        favoritePrompts
                )
        );
    }

    /**
     * Retrieves all favorite prompts for the currently authenticated user.
     *
     * @return a list of {@link FavoritePromptDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<FavoritePromptDto> getFavoritePrompts() {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        return getFavoritePromptsFromMetadata(userMetadata);
    }

    /**
     * Retrieves a specific favorite prompt by its prompt ID.
     *
     * @param promptId the prompt ID of the favorite prompt to retrieve.
     * @return the {@link FavoritePromptDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public FavoritePromptDto getFavoritePromptById(@NotNull String promptId) {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);
        Optional<FavoritePromptDto> favoritePrompt = favoritePrompts.stream()
                .filter(fp -> fp.promptId()
                        .equals(promptId))
                .findFirst();

        return favoritePrompt.orElse(null);
    }

    /**
     * Clears all favorite prompts for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void clearFavoritePrompts() {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);
        if (favoritePrompts.isEmpty()) {
            return;
        }

        auth0ManagementService.updateUserMetadata(
                SecurityUtils.getAuthenticatedUserId(),
                Map.of(
                        FAVORITE_PROMPTS_KEY,
                        new ArrayList<>()
                )
        );
    }

    /**
     * Checks if a prompt is already a favorite for the currently authenticated user.
     *
     * @param promptId the prompt ID to check.
     * @return true if the prompt is a favorite, false otherwise.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public boolean isFavoritePrompt(@NotNull String promptId) {
        Map<String, Object> userMetadata =
                userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        List<FavoritePromptDto> favoritePrompts =
                getFavoritePromptsFromMetadata(userMetadata);

        return favoritePrompts.stream()
                .anyMatch(fp -> fp.promptId().equals(promptId));
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
            List<Map<String, Object>> favoritePromptsMap =
                    (List<Map<String, Object>>) userMetadata.get(
                            FAVORITE_PROMPTS_KEY);
            favoritePrompts = favoritePromptsMap.stream()
                    .map(map -> objectMapper.convertValue(
                            map,
                            FavoritePromptDto.class
                    ))
                    .toList();
        }
        return favoritePrompts;
    }
}