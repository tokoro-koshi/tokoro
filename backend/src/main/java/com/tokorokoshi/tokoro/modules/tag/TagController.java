package com.tokorokoshi.tokoro.modules.tag;

import com.tokorokoshi.tokoro.modules.ai.Response;
import com.tokorokoshi.tokoro.modules.tag.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tag.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <h3>TagController</h1>
 * <p>
 * This class is a REST controller that handles requests for generating tags from messages.
 * </p>
 * <p>
 * WARNING: This class is for demonstration purposes only!
 * </p>
 * <p>
 * TODO: Delete in a real application
 * </p>
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(
            value = "/single",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> getTag(
            @RequestBody Map<String, String> requestBody
    ) {
        String message = requestBody.get("message");
        String conversationId = requestBody.get("conversationId");
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        Response<TagDto> response = tagService.generateTag(message, conversationId);
        if (response.isSuccessful()) {
            return ResponseEntity.ok(response.getContent());
        } else {
            return ResponseEntity.badRequest().body(response.getRefusal());
        }
    }

    @PostMapping(
            value = {"", "/"},
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> getTags(
            @RequestBody Map<String, String> requestBody
    ) {
        String message = requestBody.get("message");
        String conversationId = requestBody.get("conversationId");
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        Response<TagsDto> response = tagService.generateTags(message, conversationId);
        if (response.isSuccessful()) {
            return ResponseEntity.ok(response.getContent());
        } else {
            return ResponseEntity
                    .status(response.getRefusalStatus())
                    .body(response.getRefusal());
        }
    }
}
