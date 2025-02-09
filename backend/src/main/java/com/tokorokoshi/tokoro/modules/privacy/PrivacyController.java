package com.tokorokoshi.tokoro.modules.privacy;

import com.tokorokoshi.tokoro.modules.privacy.dto.CreateUpdatePrivacyDto;
import com.tokorokoshi.tokoro.modules.privacy.dto.PrivacyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/privacy")
@Tag(name = "Privacy", description = "API for managing Privacy information")
public class PrivacyController {

    private final PrivacyService privacyService;
    private final Logger logger;

    @Autowired
    public PrivacyController(PrivacyService privacyService) {
        this.privacyService = privacyService;
        this.logger = Logger.getLogger(PrivacyController.class.getName());
    }

    /**
     * Retrieves the Privacy information.
     *
     * @return the Privacy information
     */
    @GetMapping
    @Operation(summary = "Get Privacy Information")
    public ResponseEntity<PrivacyDto> getPrivacy() {
        PrivacyDto privacyDto = privacyService.getPrivacy();
        if (privacyDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(privacyDto);
    }

    @Operation(
            summary = "Create the Privacy information",
            description = "Accepts a request with JSON data to create the Privacy information, and returns the created Privacy information"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrivacyDto> createPrivacy(
            @Parameter(description = "The Privacy information to create", required = true)
            @RequestBody
            CreateUpdatePrivacyDto createUpdatePrivacyDto
    ) {
        try {
            PrivacyDto privacyDto = this.privacyService.createPrivacy(createUpdatePrivacyDto);
            return ResponseEntity.ok(privacyDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Update the Privacy information",
            description = "Accepts a request with JSON data to update the Privacy information, and returns the updated Privacy information"
    )
    @PutMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrivacyDto> updatePrivacy(
            @Parameter(description = "The Privacy information to update", required = true)
            @RequestBody
            CreateUpdatePrivacyDto createUpdatePrivacyDto
    ) {
        try {
            PrivacyDto privacyDto = this.privacyService.updatePrivacy(createUpdatePrivacyDto);
            return ResponseEntity.ok(privacyDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes the Privacy information.
     *
     * @return a response indicating success
     */
    @DeleteMapping
    @Operation(summary = "Delete Privacy Information")
    public ResponseEntity<Void> deletePrivacy() {
        try {
            privacyService.deletePrivacy();
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
