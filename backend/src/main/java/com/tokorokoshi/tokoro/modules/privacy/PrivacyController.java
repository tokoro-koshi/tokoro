package com.tokorokoshi.tokoro.modules.privacy;

import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.privacy.dto.CreateUpdatePrivacyDto;
import com.tokorokoshi.tokoro.modules.privacy.dto.PrivacyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/privacy")
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
            throw new NotFoundException("Privacy information not found");
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
            PrivacyDto privacyDto = privacyService.createPrivacy(createUpdatePrivacyDto);
            return ResponseEntity.ok(privacyDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
            PrivacyDto privacyDto = privacyService.updatePrivacy(createUpdatePrivacyDto);
            return ResponseEntity.ok(privacyDto);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Privacy information not found");
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
            throw new NotFoundException("Privacy information not found");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
