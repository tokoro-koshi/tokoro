package com.tokorokoshi.tokoro.modules.user.preferences;

import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.preferences.InvalidPreferenceException;
import com.tokorokoshi.tokoro.modules.exceptions.preferences.NoPreferencesException;
import com.tokorokoshi.tokoro.modules.user.preferences.dto.PreferencesDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PreferencesService.class);

    private static final String PREFERENCES_KEY = "preferences";

    private final Auth0UserDataService auth0UserDataService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PreferencesService(Auth0UserDataService auth0UserDataService, ObjectMapper objectMapper) {
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
     * Sets the user preferences for the currently authenticated user.
     *
     * @param preferencesDto the preferences to set.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPreferenceException if the preferences DTO is invalid.
     */
    public void setPreferences(@Valid PreferencesDto preferencesDto) throws Auth0ManagementException {
        validatePreferencesDto(preferencesDto);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        userMetadata.put(PREFERENCES_KEY, objectMapper.convertValue(preferencesDto, Map.class));
        auth0UserDataService.updateAuthenticatedUserMetadata(userMetadata);
        log.info("Set preferences for user {}", user.getId());
    }

    /**
     * Retrieves the preferences for the currently authenticated user.
     *
     * @return the {@link PreferencesDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws NoPreferencesException if no preferences are found for the user.
     */
    @SuppressWarnings("unchecked")
    public PreferencesDto getPreferences() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        if (userMetadata.containsKey(PREFERENCES_KEY)) {
            Map<String, Object> preferencesMap = (Map<String, Object>) userMetadata.get(PREFERENCES_KEY);
            return objectMapper.convertValue(preferencesMap, PreferencesDto.class);
        }

        log.warn("No preferences found for user {}", user.getId());
        throw new NoPreferencesException("No preferences found for user " + user.getId());
    }

    /**
     * Updates the language preference for the currently authenticated user.
     *
     * @param language the new language preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPreferenceException if the language is invalid.
     */
    public void updateLanguagePreference(@NotNull @NotBlank String language) throws Auth0ManagementException {
        validateLanguage(language);

        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            log.warn("No preferences found for user {}. Creating new preferences with language {}",
                    auth0UserDataService.getAuthenticatedUserDetails().getId(), language);
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
        log.info("Updated language preference for user {}", auth0UserDataService.getAuthenticatedUserDetails().getId());
    }

    /**
     * Updates the categories preference for the currently authenticated user.
     *
     * @param categories the new categories' preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPreferenceException if the categories list is invalid.
     */
    public void updateCategoriesPreference(@NotNull @NotEmpty List<@NotEmpty @NotBlank String> categories) throws Auth0ManagementException {
        validateCategories(categories);

        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            log.warn("No preferences found for user {}. Creating new preferences with categories {}",
                    auth0UserDataService.getAuthenticatedUserDetails().getId(), categories);
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
        log.info("Updated categories preference for user {}", auth0UserDataService.getAuthenticatedUserDetails().getId());
    }

    /**
     * Updates the timezone preference for the currently authenticated user.
     *
     * @param timezone the new timezone preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidPreferenceException if the timezone is invalid.
     */
    public void updateTimezonePreference(@NotNull @NotBlank String timezone) throws Auth0ManagementException {
        validateTimezone(timezone);

        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            log.warn("No preferences found for user {}. Creating new preferences with timezone {}",
                    auth0UserDataService.getAuthenticatedUserDetails().getId(), timezone);
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
        log.info("Updated timezone preference for user {}", auth0UserDataService.getAuthenticatedUserDetails().getId());
    }

    /**
     * Updates the notifications enabled preference for the currently authenticated user.
     *
     * @param notificationsEnabled the new notifications enabled preference.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateNotificationsEnabledPreference(@NotNull boolean notificationsEnabled) throws Auth0ManagementException {
        PreferencesDto preferences = getPreferences();
        if (preferences == null) {
            log.warn("No preferences found for user {}. Creating new preferences with notifications enabled {}",
                    auth0UserDataService.getAuthenticatedUserDetails().getId(), notificationsEnabled);
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
        log.info("Updated notifications enabled preference for user {}", auth0UserDataService.getAuthenticatedUserDetails().getId());
    }

    /**
     * Clears all preferences for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoPreferencesException if there are no preferences to clear.
     */
    public void clearPreferences() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        if (!userMetadata.containsKey(PREFERENCES_KEY)) {
            log.warn("No preferences to clear for user {}", user.getId());
            throw new NoPreferencesException("No preferences to clear for user " + user.getId());
        }

        userMetadata.remove(PREFERENCES_KEY);
        auth0UserDataService.updateAuthenticatedUserMetadata(userMetadata);
        log.info("Cleared all preferences for user {}", user.getId());
    }

    /**
     * Validates that the provided PreferencesDto is not null.
     *
     * @param preferencesDto the DTO to validate.
     * @throws InvalidPreferenceException if the DTO is null.
     */
    private void validatePreferencesDto(PreferencesDto preferencesDto) {
        if (preferencesDto == null) {
            log.error("PreferencesDto must not be null");
            throw new InvalidPreferenceException("PreferencesDto must not be null");
        }
    }

    /**
     * Validates that the provided language is not null or empty.
     *
     * @param language the language to validate.
     * @throws InvalidPreferenceException if the language is null or empty.
     */
    private void validateLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            log.error("Language must not be null or empty");
            throw new InvalidPreferenceException("Language must not be null or empty");
        }
    }

    /**
     * Validates that the provided categories list is not null, empty, or contains invalid entries.
     *
     * @param categories the categories list to validate.
     * @throws InvalidPreferenceException if the categories list is invalid.
     */
    private void validateCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            log.error("Categories list must not be null or empty");
            throw new InvalidPreferenceException("Categories list must not be null or empty");
        }
        for (String category : categories) {
            if (category == null || category.trim().isEmpty()) {
                log.error("Each category in the categories list must not be null or empty");
                throw new InvalidPreferenceException("Each category in the categories list must not be null or empty");
            }
        }
    }

    /**
     * Validates that the provided timezone is not null or empty.
     *
     * @param timezone the timezone to validate.
     * @throws InvalidPreferenceException if the timezone is null or empty.
     */
    private void validateTimezone(String timezone) {
        if (timezone == null || timezone.trim().isEmpty()) {
            log.error("Timezone must not be null or empty");
            throw new InvalidPreferenceException("Timezone must not be null or empty");
        }
    }
}