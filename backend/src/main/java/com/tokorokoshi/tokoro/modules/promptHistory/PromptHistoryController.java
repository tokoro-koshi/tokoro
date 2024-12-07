package com.tokorokoshi.tokoro.modules.promptHistory;

import com.tokorokoshi.tokoro.modules.promptHistory.dto.CreateUpdatePromptHistoryDto;
import com.tokorokoshi.tokoro.modules.promptHistory.dto.PromptHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/prompt-history")
public class PromptHistoryController {
    private final PromptHistoryService promptHistoryService;

    public PromptHistoryController(PromptHistoryService promptHistoryService) {
        this.promptHistoryService = promptHistoryService;
    }

    //CRUD
    @PostMapping(value = {"", "/"},
                consumes = APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PromptHistoryDto> savePromptHistory(@RequestBody CreateUpdatePromptHistoryDto promptHistory) {
        return ResponseEntity.ok(promptHistoryService.savePromptHistory(promptHistory));
    }

    @GetMapping(value = "/{id}",
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PromptHistoryDto> findPromptHistoryById(@PathVariable String id) {
       return ResponseEntity.ok(promptHistoryService.findPromptHistoryById(id));
    }

    @GetMapping(value = {"/all", "list"},
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PromptHistoryDto>> findAllPromptHistories() {
        return ResponseEntity.ok(promptHistoryService.findAllPromptHistories());
    }

    @PutMapping(value = "/{id}",
                consumes = APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PromptHistoryDto> updatePromptHistory(
            @RequestBody CreateUpdatePromptHistoryDto promptHistory,
            @PathVariable String id
    ) {
        PromptHistoryDto updatedPromptHistory = promptHistoryService.updatePromptHistory(id, promptHistory);
        if(updatedPromptHistory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPromptHistory);
    }

    @DeleteMapping(value = "/{id}")
    public void deletePromptHistory(@PathVariable String id) {
        promptHistoryService.deletePromptHistory(id);
    }
}
