package com.tokorokoshi.tokoro.modules.testimonials;

import com.tokorokoshi.tokoro.database.Testimonial;
import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.modules.testimonials.dto.CreateUpdateTestimonialDto;
import com.tokorokoshi.tokoro.modules.testimonials.dto.TestimonialDto;
import com.tokorokoshi.tokoro.modules.testimonials.dto.UpdateTestimonialStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Testimonials", description = "API for managing testimonials")
@RestController
@RequestMapping("/api/testimonials")
public class TestimonialsController {

    private final TestimonialsService testimonialsService;
    private final Logger logger;
    private final PagedResourcesAssembler<TestimonialDto> pagedResourcesAssembler;

    public TestimonialsController(
            TestimonialsService testimonialsService,
            PagedResourcesAssembler<TestimonialDto> pagedResourcesAssembler
    ) {
        this.testimonialsService = testimonialsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(TestimonialsController.class.getName());
    }

    @Operation(
            summary = "Save a new testimonial",
            description = "Accepts a request with JSON data to save a new testimonial, and returns the saved testimonial"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TestimonialDto> saveTestimonial(
            @Parameter(
                    description = "The testimonial to save",
                    required = true
            )
            @RequestBody
            CreateUpdateTestimonialDto testimonial
    ) {
        try {
            TestimonialDto testimonialDto = testimonialsService.saveTestimonial(testimonial);
            return ResponseEntity.ok(testimonialDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Get a testimonial by ID",
            description = "Returns the testimonial with the given ID"
    )
    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TestimonialDto> getTestimonialById(
            @Parameter(
                    description = "The ID of the testimonial to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        var testimonial = testimonialsService.getTestimonialById(id);
        if (testimonial == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(testimonial);
    }

    @Operation(
            summary = "Get all testimonials",
            description = "Returns a paginated list of all testimonials"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PaginationDto<TestimonialDto>> getAllTestimonials(
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var testimonials = testimonialsService.getAllTestimonials(pageable);
        var pagination = PaginationDto.fromEntityModel(
                pagedResourcesAssembler.toModel(testimonials)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get testimonials for a specific user",
            description = "Returns a paginated list of testimonials associated with a specific user ID."
    )
    @GetMapping(
            value = "/user/{userId}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PaginationDto<TestimonialDto>> getUserTestimonials(
            @Parameter(
                    description = "The ID of the user whose testimonials to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String userId,
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var testimonials = testimonialsService.getUserTestimonials(userId, pageable);
        var pagination = PaginationDto.fromEntityModel(
                pagedResourcesAssembler.toModel(testimonials)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get testimonials by status",
            description = "Returns a paginated list of testimonials with a specific status"
    )
    @GetMapping(
            value = "/by-status",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PaginationDto<TestimonialDto>> getTestimonialsByStatus(
            @Parameter(
                    description = "The status of the testimonials to get",
                    required = true,
                    example = "PENDING"
            )
            @RequestParam
            Testimonial.Status status,
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var testimonials = testimonialsService.getTestimonialsByStatus(status, pageable);
        var pagination = PaginationDto.fromEntityModel(
                pagedResourcesAssembler.toModel(testimonials)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get random testimonials with APPROVED status",
            description = "Returns a list of random testimonials with APPROVED status"
    )
    @GetMapping(
            value = "/random/{count}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TestimonialDto>> getRandomApprovedTestimonials(
            @Parameter(
                    description = "The number of random testimonials to get",
                    required = true,
                    example = "5"
            )
            @PathVariable
            int count
    ) {
        List<TestimonialDto> randomTestimonials = testimonialsService.getRandomApprovedTestimonials(count);
        return ResponseEntity.ok(randomTestimonials);
    }

    @Operation(
            summary = "Update a testimonial",
            description = "Accepts a request with JSON data to update a testimonial, and returns the updated testimonial"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TestimonialDto> updateTestimonial(
            @Parameter(
                    description = "The ID of the testimonial to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The testimonial to update", required = true)
            @RequestBody
            CreateUpdateTestimonialDto testimonialDto
    ) {
        TestimonialDto updatedTestimonial = testimonialsService.updateTestimonial(id, testimonialDto);
        return ResponseEntity.ok(updatedTestimonial);
    }

    @Operation(
            summary = "Change testimonial status",
            description = "Changes the status of a testimonial"
    )
    @PatchMapping(
            value = "/{id}/status",
            consumes = APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> changeTestimonialStatus(
            @Parameter(
                    description = "The ID of the testimonial to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The new status for the testimonial",
                    required = true
            )
            @RequestBody
            UpdateTestimonialStatusDto statusDto
    ) {
        Testimonial.Status status = statusDto.status();
        testimonialsService.changeTestimonialStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a testimonial",
            description = "Deletes the testimonial with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTestimonial(
            @Parameter(
                    description = "The ID of the testimonial to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        try {
            testimonialsService.deleteTestimonial(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}