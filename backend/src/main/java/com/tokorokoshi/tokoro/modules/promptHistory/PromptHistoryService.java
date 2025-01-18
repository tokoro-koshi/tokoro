package com.tokorokoshi.tokoro.modules.promptHistory;

import com.tokorokoshi.tokoro.database.PromptHistory;
import com.tokorokoshi.tokoro.modules.promptHistory.dto.CreateUpdatePromptHistoryDto;
import com.tokorokoshi.tokoro.modules.promptHistory.dto.PromptHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptHistoryService {
    private final MongoTemplate mongoTemplate;
    private final PromptHistoryMapper promptHistoryMapper;

    @Autowired
    public PromptHistoryService(
        MongoTemplate mongoTemplate,
        PromptHistoryMapper promptHistoryMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.promptHistoryMapper = promptHistoryMapper;
    }

    public PromptHistoryDto savePromptHistory(
        CreateUpdatePromptHistoryDto promptHistory
    ) {
        PromptHistory promptHistorySchema = promptHistoryMapper
            .toPromptHistorySchema(promptHistory);
        return promptHistoryMapper.toPromptHistoryDto(
            mongoTemplate.save(promptHistorySchema)
        );
    }

    public PromptHistoryDto findPromptHistoryById(String id) {
        return promptHistoryMapper.toPromptHistoryDto(
            mongoTemplate.findById(id, PromptHistory.class)
        );
    }

    public List<PromptHistoryDto> findAllPromptHistories() {
        return promptHistoryMapper.toPromptHistoryDto(
            mongoTemplate.findAll(PromptHistory.class)
        );
    }

    public PromptHistoryDto updatePromptHistory(
        String id,
        CreateUpdatePromptHistoryDto promptHistory
    ) {
        if (findPromptHistoryById(id) == null) {
            return null;
        }
        PromptHistory promptHistorySchema = promptHistoryMapper
            .toPromptHistorySchema(promptHistory);
        return promptHistoryMapper.toPromptHistoryDto(
            mongoTemplate.save(promptHistorySchema.withId(id))
        );
    }

    public void deletePromptHistory(String id) {
        mongoTemplate.remove(
            promptHistoryMapper.toPromptHistorySchema(findPromptHistoryById(id))
        );
    }
}
