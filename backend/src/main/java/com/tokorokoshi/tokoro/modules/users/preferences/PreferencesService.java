package com.tokorokoshi.tokoro.modules.users.preferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.users.UserService;
import com.tokorokoshi.tokoro.modules.users.preferences.dto.PreferencesDto;
import com.tokorokoshi.tokoro.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class responsible for managing user preferences.
 * This class handles operations such as setting, retrieving, updating, and clearing user preferences.
 */
@Service
public class PreferencesService {

    private static final String PREFERENCES_KEY = "preferences";

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Auth0ManagementService auth0ManagementService;

    @Autowired
    public PreferencesService(UserService userService, ObjectMapper objectMapper, Auth0ManagementService auth0ManagementService) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.auth0ManagementService = auth0ManagementService;
    }

    /**
     * Sets the user preferences for the currently authenticated user.
     *
     * @param preferencesDto the preferences to set.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void setPreferences(@Valid PreferencesDto preferencesDto) {
        Map<String, Object> userMetadata = userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        userMetadata.put(PREFERENCES_KEY, objectMapper.convertValue(preferencesDto, Map.class));
        auth0ManagementService.updateUserMetadata(SecurityUtils.getAuthenticatedUserId(), userMetadata);
    }

    /**
     * Retrieves the preferences for the currently authenticated user.
     *
     * @return the {@link PreferencesDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    @SuppressWarnings("unchecked")
    public PreferencesDto getPreferences() {
        Map<String, Object> userMetadata = userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        if (userMetadata.containsKey(PREFERENCES_KEY)) {
            Map<String, Object> preferencesMap = (Map<String, Object>) userMetadata.get(PREFERENCES_KEY);
            return objectMapper.convertValue(preferencesMap, PreferencesDto.class);
        }
        return null;
    }

    /**
     * Updates the language preference for the currently authenticated user.
     *
     * @param language the new language preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateLanguagePreference(@NotNull @NotBlank String language) {
        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            setPreferences(new PreferencesDto(language, List.of(), "UTC", true));
            return;
        }

        PreferencesDto updatedPreferences = new PreferencesDto(
                language,
                preferences.categories(),
                preferences.timezone(),
                preferences.notificationsEnabled()
        );

        setPreferences(updatedPreferences);
    }

    /**
     * Updates the categories preference for the currently authenticated user.
     *
     * @param categories the new categories' preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateCategoriesPreference(@NotNull @NotEmpty List<@NotEmpty @NotBlank String> categories) {
        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            setPreferences(new PreferencesDto("en", categories, "UTC", true));
            return;
        }

        PreferencesDto updatedPreferences = new PreferencesDto(
                preferences.language(),
                categories,
                preferences.timezone(),
                preferences.notificationsEnabled()
        );

        setPreferences(updatedPreferences);
    }

    /**
     * Updates the timezone preference for the currently authenticated user.
     *
     * @param timezone the new timezone preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateTimezonePreference(@NotNull @NotBlank String timezone) {
        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            setPreferences(new PreferencesDto("en", List.of(), timezone, true));
            return;
        }

        PreferencesDto updatedPreferences = new PreferencesDto(
                preferences.language(),
                preferences.categories(),
                timezone,
                preferences.notificationsEnabled()
        );

        setPreferences(updatedPreferences);
    }

    /**
     * Updates the notifications enabled preference for the currently authenticated user.
     *
     * @param notificationsEnabled the new notifications enabled preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateNotificationsEnabledPreference(@NotNull boolean notificationsEnabled) {
        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            setPreferences(new PreferencesDto("en", List.of(), "UTC", notificationsEnabled));
            return;
        }

        PreferencesDto updatedPreferences = new PreferencesDto(
                preferences.language(),
                preferences.categories(),
                preferences.timezone(),
                notificationsEnabled
        );

        setPreferences(updatedPreferences);
    }

    /**
     * Clears all preferences for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void clearPreferences() {
        Map<String, Object> userMetadata = userService.getUserMetadata(SecurityUtils.getAuthenticatedUserId());

        if (!userMetadata.containsKey(PREFERENCES_KEY)) {
            return;
        }

        userMetadata.remove(PREFERENCES_KEY);
        auth0ManagementService.updateUserMetadata(SecurityUtils.getAuthenticatedUserId(), userMetadata);
    }
}