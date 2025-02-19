package com.tokorokoshi.tokoro.modules.features;

import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.features.dto.CreateUpdateFeatureDto;
import com.tokorokoshi.tokoro.modules.features.dto.FeatureDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(
        name = "Features",
        description = "API for managing features"
)
@RestController
@RequestMapping("/features")
public class FeaturesController {

    private final FeaturesService featuresService;
    private final Logger logger;

    @Autowired
    public FeaturesController(FeaturesService featuresService) {
        this.featuresService = featuresService;
        this.logger = Logger.getLogger(FeaturesController.class.getName());
    }

    @Operation(
            summary = "Create a new feature",
            description = "Accepts a request with form data to create a new feature, and returns the created feature"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeatureDto> create(
            @Parameter(
                    description = "The feature to create",
                    required = true
            )
            @ModelAttribute
            CreateUpdateFeatureDto feature
    ) {
        try {
            FeatureDto featureDto = featuresService.saveFeature(feature);
            return ResponseEntity.ok(featureDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get a feature by ID",
            description = "Returns the feature with the given ID"
    )
    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeatureDto> get(
            @Parameter(
                    description = "The ID of the feature to get",
                    required = true,
                    example = "43fade3jad3dla3dl234fdf"
            )
            @PathVariable
            String id
    ) {
        FeatureDto feature = featuresService.findFeatureById(id);
        if (feature == null) {
            throw new NotFoundException("Feature not found");
        }
        return ResponseEntity.ok(feature);
    }

    @Operation(
            summary = "Get all features",
            description = "Returns a list of all features"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<FeatureDto>> getAllFeatures() {
        List<FeatureDto> features = featuresService.findAllFeatures();
        return ResponseEntity.ok(features);
    }

    @Operation(
            summary = "Update a feature",
            description = "Accepts a request with form data to update a feature, and returns the updated feature"
    )
    @PutMapping(
            value = "/{id}",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeatureDto> update(
            @Parameter(
                    description = "The feature to update",
                    required = true
            )
            @ModelAttribute
            CreateUpdateFeatureDto feature,
            @Parameter(
                    description = "The ID of the feature to update",
                    required = true,
                    example = "43fade3jad3dla3dl234fdf"
            )
            @PathVariable
            String id
    ) {
        try {
            FeatureDto updatedFeature = featuresService.updateFeature(id, feature);
            if (updatedFeature == null) {
                throw new NotFoundException("Feature not found");
            }
            return ResponseEntity.ok(updatedFeature);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Delete a feature",
            description = "Deletes the feature with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @Parameter(
                    description = "The ID of the feature to delete",
                    required = true,
                    example = "43fade3jad3dla3dl234fdf"
            )
            @PathVariable
            String id
    ) {
        try {
            featuresService.deleteFeature(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Feature not found");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}