package com.tokorokoshi.tokoro.modules.user.preferences;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user preferences.
 * This controller provides endpoints to set, retrieve, update, and clear user preferences.
 */
@RestController
@RequestMapping("/api/v1/users/preferences")
public class PreferencesController {

    private static final Logger log = LoggerFactory.getLogger(PreferencesController.class);

    private final PreferencesService preferencesService;

    @Autowired
    public PreferencesController(PreferencesService preferencesService) {
        if (preferencesService == null) {
            throw new IllegalArgumentException("PreferencesService must not be null");
        }
        this.preferencesService = preferencesService;
    }

    /**
     * Sets the user preferences for the currently authenticated user.
     *
     * @param preferencesDto the preferences to set.
     * @return a response indicating success or failure.
     */
    @PostMapping
    public ResponseEntity<String> setPreferences(@Valid @RequestBody PreferencesDto preferencesDto) {
        try {
            preferencesService.setPreferences(preferencesDto);
            log.info("Preferences set successfully for user");
            return ResponseEntity.ok("Preferences set successfully");
        } catch (InvalidPreferenceException e) {
            log.error("Invalid preferences provided", e);
            return ResponseEntity.badRequest().body("Invalid preferences: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error setting preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to set preferences: " + e.getMessage());
        }
    }

    /**
     * Retrieves the preferences for the currently authenticated user.
     *
     * @return the {@link PreferencesDto} object if found, otherwise a not found response.
     */
    @GetMapping
    public ResponseEntity<PreferencesDto> getPreferences() {
        try {
            PreferencesDto preferences = preferencesService.getPreferences();
            log.info("Preferences retrieved successfully for user");
            return ResponseEntity.ok(preferences);
        } catch (NoPreferencesException e) {
            log.warn("No preferences found for user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return null for not found
        } catch (Exception e) {
            log.error("Error retrieving preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Updates the language preference for the currently authenticated user.
     *
     * @param language the new language preference.
     * @return a response indicating success or failure.
     */
    @PutMapping("/language")
    public ResponseEntity<String> updateLanguagePreference(
            @RequestParam @NotNull @NotBlank String language) {
        try {
            preferencesService.updateLanguagePreference(language);
            log.info("Language preference updated successfully for user");
            return ResponseEntity.ok("Language preference updated successfully");
        } catch (InvalidPreferenceException e) {
            log.error("Invalid language provided", e);
            return ResponseEntity.badRequest().body("Invalid language: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error updating language preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update language preference: " + e.getMessage());
        }
    }

    /**
     * Updates the categories preference for the currently authenticated user.
     *
     * @param categories the new categories' preference.
     * @return a response indicating success or failure.
     */
    @PutMapping("/categories")
    public ResponseEntity<String> updateCategoriesPreference(
            @RequestParam @NotNull @NotEmpty List<@NotEmpty @NotBlank String> categories) {
        try {
            preferencesService.updateCategoriesPreference(categories);
            log.info("Categories preference updated successfully for user");
            return ResponseEntity.ok("Categories preference updated successfully");
        } catch (InvalidPreferenceException e) {
            log.error("Invalid categories provided", e);
            return ResponseEntity.badRequest().body("Invalid categories: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error updating categories preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update categories preference: " + e.getMessage());
        }
    }

    /**
     * Updates the timezone preference for the currently authenticated user.
     *
     * @param timezone the new timezone preference.
     * @return a response indicating success or failure.
     */
    @PutMapping("/timezone")
    public ResponseEntity<String> updateTimezonePreference(
            @RequestParam @NotNull @NotBlank String timezone) {
        try {
            preferencesService.updateTimezonePreference(timezone);
            log.info("Timezone preference updated successfully for user");
            return ResponseEntity.ok("Timezone preference updated successfully");
        } catch (InvalidPreferenceException e) {
            log.error("Invalid timezone provided", e);
            return ResponseEntity.badRequest().body("Invalid timezone: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error updating timezone preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update timezone preference: " + e.getMessage());
        }
    }

    /**
     * Updates the notifications enabled preference for the currently authenticated user.
     *
     * @param notificationsEnabled the new notifications enabled preference.
     * @return a response indicating success or failure.
     */
    @PutMapping("/notifications-enabled")
    public ResponseEntity<String> updateNotificationsEnabledPreference(
            @RequestParam @NotNull boolean notificationsEnabled) {
        try {
            preferencesService.updateNotificationsEnabledPreference(notificationsEnabled);
            log.info("Notifications enabled preference updated successfully for user");
            return ResponseEntity.ok("Notifications enabled preference updated successfully");
        } catch (Exception e) {
            log.error("Error updating notifications enabled preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update notifications enabled preference: " + e.getMessage());
        }
    }

    /**
     * Clears all preferences for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @DeleteMapping
    public ResponseEntity<String> clearPreferences() {
        try {
            preferencesService.clearPreferences();
            log.info("All preferences cleared successfully for user");
            return ResponseEntity.ok("All preferences cleared successfully");
        } catch (NoPreferencesException e) {
            log.warn("No preferences to clear for user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No preferences to clear");
        } catch (Exception e) {
            log.error("Error clearing preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear preferences: " + e.getMessage());
        }
    }
}