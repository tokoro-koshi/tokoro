package com.tokorokoshi.tokoro.modules.user.preferences;

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
            return ResponseEntity.ok("Preferences set successfully");
        } catch (Exception e) {
            log.error("Error setting preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to set preferences");
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
            if (preferences == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.ok("Language preference updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update language preference");
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
            return ResponseEntity.ok("Categories preference updated successfully");
        } catch (Exception e) {
            log.error("Error updating categories preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update categories preference");
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
            return ResponseEntity.ok("Timezone preference updated successfully");
        } catch (Exception e) {
            log.error("Error updating timezone preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update timezone preference");
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
            return ResponseEntity.ok("Notifications enabled preference updated successfully");
        } catch (Exception e) {
            log.error("Error updating notifications enabled preference", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update notifications enabled preference");
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
            return ResponseEntity.ok("All preferences cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear preferences");
        }
    }
}