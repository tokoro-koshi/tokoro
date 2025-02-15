package com.tokorokoshi.tokoro.modules.search;

import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Search", description = "API for search places")
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(
            summary = "Search places by generated tags",
            description = "Returns a list of places based on the search query"
    )
    @GetMapping(value = "/{query}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<PlaceDto>> search(
            @Parameter(
                    description = "The search query",
                    required = true,
                    example = "restaurant"
            )
            @PathVariable
            String query) {
        return ResponseEntity.ok(searchService.search(query).getContent());
    }
}
