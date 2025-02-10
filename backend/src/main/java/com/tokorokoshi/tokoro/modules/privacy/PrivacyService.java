package com.tokorokoshi.tokoro.modules.privacy;

import com.tokorokoshi.tokoro.database.Privacy;
import com.tokorokoshi.tokoro.modules.privacy.dto.CreateUpdatePrivacyDto;
import com.tokorokoshi.tokoro.modules.privacy.dto.PrivacyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrivacyService {

    private final MongoTemplate mongoTemplate;
    private final PrivacyMapper privacyMapper;

    @Autowired
    public PrivacyService(
            MongoTemplate mongoTemplate,
            PrivacyMapper privacyMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.privacyMapper = privacyMapper;
    }

    /**
     * Retrieves the single Privacy document.
     *
     * @return the Privacy document
     */
    public PrivacyDto getPrivacy() {
        Optional<Privacy> privacyOptional = Optional.ofNullable(mongoTemplate.findOne(new Query(), Privacy.class));
        return privacyOptional.map(privacyMapper::toPrivacyDto).orElse(null);
    }

    /**
     * Creates a new Privacy document.
     *
     * @param createUpdatePrivacyDto the data to create
     * @return the created Privacy document
     */
    public PrivacyDto createPrivacy(CreateUpdatePrivacyDto createUpdatePrivacyDto) {
        // Map DTO to Privacy entity
        Privacy privacy = privacyMapper.toPrivacy(createUpdatePrivacyDto);

        // Save to MongoDB
        Privacy savedPrivacy = mongoTemplate.save(privacy);
        return privacyMapper.toPrivacyDto(savedPrivacy);
    }

    /**
     * Updates an existing Privacy document.
     *
     * @param createUpdatePrivacyDto the data to update
     * @return the updated Privacy document
     */
    public PrivacyDto updatePrivacy(CreateUpdatePrivacyDto createUpdatePrivacyDto) {
        // Check if a Privacy document already exists
        Privacy existingPrivacy = mongoTemplate.findOne(new Query(), Privacy.class);
        if (existingPrivacy == null) {
            throw new IllegalArgumentException("Privacy document not found");
        }

        // Map DTO to Privacy entity and retain the existing ID
        Privacy privacy = privacyMapper.toPrivacy(createUpdatePrivacyDto).withId(existingPrivacy.id());

        // Save to MongoDB
        Privacy savedPrivacy = mongoTemplate.save(privacy);
        return privacyMapper.toPrivacyDto(savedPrivacy);
    }

    /**
     * Deletes the Privacy document.
     */
    public void deletePrivacy() {
        Optional<Privacy> privacyOptional = Optional.ofNullable(mongoTemplate.findOne(new Query(), Privacy.class));
        if (privacyOptional.isPresent()) {
            mongoTemplate.remove(privacyOptional.get());
        } else {
            throw new IllegalArgumentException("Privacy document not found");
        }
    }
}