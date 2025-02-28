package com.tokorokoshi.tokoro.modules.users.preferences;

import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.users.preferences.dto.PreferencesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.List;

/**
 * REST controller for managing user preferences.
 * This controller provides endpoints to set, retrieve, update, and clear user preferences.
 */
@Tag(
        name = "User Preferences",
        description = "API for managing user preferences"
)
@RestController
@RequestMapping("/users/preferences")
public class PreferencesController {
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
    @Operation(
            summary = "Set user preferences",
            description = "Accepts a request with a JSON body to set user preferences"
    )
    @PostMapping
    public ResponseEntity<String> setPreferences(
            @Parameter(
                    description = "The preferences to set",
                    required = true
            )
            @Valid
            @RequestBody
            PreferencesDto preferencesDto
    ) {
        try {
            preferencesService.setPreferences(preferencesDto);
            return ResponseEntity.ok("Preferences set successfully");
        } catch (Exception e) {
            throw new ServerErrorException("Failed to set preferences", e);
        }
    }

    /**
     * Retrieves the preferences for the currently authenticated user.
     *
     * @return the {@link PreferencesDto} object if found, otherwise a not found response.
     */
    @Operation(
            summary = "Get user preferences",
            description = "Returns the user preferences"
    )
    @GetMapping
    public ResponseEntity<PreferencesDto> getPreferences() {
        try {
            PreferencesDto preferences = preferencesService.getPreferences();
            if (preferences == null) {
                throw new NotFoundException("Preferences not found");
            }
            return ResponseEntity.ok(preferences);
        } catch (Exception ex) {
            throw new ServerErrorException(ex.getMessage(), ex);
        }
    }

    /**
     * Updates the language preference for the currently authenticated user.
     *
     * @param language the new language preference.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update language preference",
            description = "Accepts a request with a query parameter to update the language preference"
    )
    @PutMapping("/language")
    public ResponseEntity<String> updateLanguagePreference(
            @Parameter(
                    description = "The new language preference",
                    required = true,
                    example = "en"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String language
    ) {
        try {
            preferencesService.updateLanguagePreference(language);
            return ResponseEntity.ok("Language preference updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update language preference",
                    ex
            );
        }
    }

    /**
     * Updates the categories preference for the currently authenticated user.
     *
     * @param categories the new categories' preference.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update categories preference",
            description = "Accepts a request with a query parameter to update the categories preference"
    )
    @PutMapping("/categories")
    public ResponseEntity<String> updateCategoriesPreference(
            @Parameter(
                    description = "The new categories preference",
                    required = true,
                    example = "[\"food\", \"travel\"]"
            )
            @RequestParam
            @NotNull
            @NotEmpty
            List<@NotEmpty @NotBlank String> categories
    ) {
        try {
            preferencesService.updateCategoriesPreference(categories);
            return ResponseEntity.ok(
                    "Categories preference updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update categories preference",
                    ex
            );
        }
    }

    /**
     * Updates the timezone preference for the currently authenticated user.
     *
     * @param timezone the new timezone preference.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update timezone preference",
            description = "Accepts a request with a query parameter to update the timezone preference"
    )
    @PutMapping("/timezone")
    public ResponseEntity<String> updateTimezonePreference(
            @Parameter(
                    description = "The new timezone preference",
                    required = true,
                    example = "America/New_York"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String timezone
    ) {
        try {
            preferencesService.updateTimezonePreference(timezone);
            return ResponseEntity.ok("Timezone preference updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update timezone preference",
                    ex
            );
        }
    }

    /**
     * Updates the notifications enabled preference for the currently authenticated user.
     *
     * @param notificationsEnabled the new notifications enabled preference.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update notifications enabled preference",
            description = "Accepts a request with a query parameter to update the notifications enabled preference"
    )
    @PutMapping("/notifications-enabled")
    public ResponseEntity<String> updateNotificationsEnabledPreference(
            @Parameter(
                    description = "The new notifications enabled preference",
                    required = true,
                    example = "true"
            )
            @RequestParam
            @NotNull
            boolean notificationsEnabled
    ) {
        try {
            preferencesService.updateNotificationsEnabledPreference(
                    notificationsEnabled);
            return ResponseEntity.ok(
                    "Notifications enabled preference updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update notifications enabled preference",
                    ex
            );
        }
    }

    /**
     * Clears all preferences for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Clear all preferences",
            description = "Clears all preferences for the currently authenticated user"
    )
    @DeleteMapping
    public ResponseEntity<String> clearPreferences() {
        try {
            preferencesService.clearPreferences();
            return ResponseEntity.ok("All preferences cleared successfully");
        } catch (Exception ex) {
            throw new ServerErrorException("Failed to clear preferences", ex);
        }
    }
}