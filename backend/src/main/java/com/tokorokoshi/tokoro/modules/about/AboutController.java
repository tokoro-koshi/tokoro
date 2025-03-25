package com.tokorokoshi.tokoro.modules.about;

import com.tokorokoshi.tokoro.modules.about.dto.AboutDto;
import com.tokorokoshi.tokoro.modules.about.dto.CreateUpdateAboutDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/about")
@Tag(name = "About", description = "API for managing About information")
public class AboutController {
    private final AboutService aboutService;
    private final Logger logger;

    @Autowired
    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
        this.logger = Logger.getLogger(AboutController.class.getName());
    }

    /**
     * Retrieves the About information.
     *
     * @return the About information
     */
    @GetMapping
    @Operation(summary = "Get About Information")
    public ResponseEntity<AboutDto> getAbout() {
        AboutDto aboutDto = aboutService.getAbout();
        if (aboutDto == null) {
            throw new NotFoundException("About information not found");
        }
        return ResponseEntity.ok(aboutDto);
    }

    @Operation(
            summary = "Create the About information",
            description = "Accepts a request with form data to create the About information, and returns the created About information"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AboutDto> createAbout(
            @Parameter(description = "The About information to create", required = true)
            @ModelAttribute
            CreateUpdateAboutDto createUpdateAboutDto
    ) {
        try {
            AboutDto aboutDto = this.aboutService.createAbout(createUpdateAboutDto);
            return ResponseEntity.ok(aboutDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Update the About information",
            description = "Accepts a request with form data to update the About information, and returns the updated About information"
    )
    @PutMapping(
            value = {"", "/"},
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AboutDto> updateAbout(
            @Parameter(description = "The About information to update", required = true)
            @ModelAttribute
            CreateUpdateAboutDto createUpdateAboutDto
    ) {
        try {
            AboutDto aboutDto = aboutService.updateAbout(createUpdateAboutDto);
            return ResponseEntity.ok(aboutDto);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("About information not found");
        }
    }

    /**
     * Deletes the About information.
     *
     * @return a response indicating success
     */
    @DeleteMapping
    @Operation(summary = "Delete About Information")
    public ResponseEntity<Void> deleteAbout() {
        try {
            aboutService.deleteAbout();
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("About information not found");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
