package com.tokorokoshi.tokoro.modules.promptHistory;

import com.tokorokoshi.tokoro.database.PromptHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptHistoryService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PromptHistoryService(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    //CRUD

    public void savePromptHistory(PromptHistory promptHistory){
        mongoTemplate.save(promptHistory);
    }


    public PromptHistory findPromptHistoryById(String id){
       return mongoTemplate.findById(id, PromptHistory.class);
    }

    public List<PromptHistory> findAllPromptHistories(){
        return mongoTemplate.findAll(PromptHistory.class);

    }

    public void updatePromptHistory(String id, PromptHistory promptHistory){
        promptHistory.setId(id);
        mongoTemplate.save(promptHistory);
    }

    public void deletePromptHistory(String id){
        mongoTemplate.remove(findPromptHistoryById(id));
    }



}
