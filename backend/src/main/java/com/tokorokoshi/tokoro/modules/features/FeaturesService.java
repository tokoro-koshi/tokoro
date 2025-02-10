package com.tokorokoshi.tokoro.modules.features;

import com.tokorokoshi.tokoro.database.Feature;
import com.tokorokoshi.tokoro.modules.features.dto.CreateUpdateFeatureDto;
import com.tokorokoshi.tokoro.modules.features.dto.FeatureDto;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service class for features.
 */
@Service
public class FeaturesService {

    private final MongoTemplate mongoTemplate;
    private final FeatureMapper featureMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public FeaturesService(
            MongoTemplate mongoTemplate,
            FeatureMapper featureMapper,
            FileStorageService fileStorageService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.featureMapper = featureMapper;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Get feature with picture URL.
     */
    private FeatureDto getFeatureWithPictureUrl(Feature feature) {
        String pictureUrl = fileStorageService.generateSignedUrl(feature.picture()).join();
        return featureMapper.toFeatureDto(feature.withPicture(pictureUrl));
    }

    /**
     * Validates the file to be an image.
     *
     * @param file file to validate
     * @return true if the file is a valid image, false otherwise
     */
    private boolean isImageFileInvalid(MultipartFile file) {
        if (file == null) {
            return true;
        }
        String mimeType = file.getContentType();
        return mimeType == null || !mimeType.startsWith("image/");
    }

    /**
     * Saves a feature.
     *
     * @param createUpdateFeatureDto the feature to save
     * @return the saved feature
     */
    public FeatureDto saveFeature(CreateUpdateFeatureDto createUpdateFeatureDto) {
        // Validate picture file
        if (isImageFileInvalid(createUpdateFeatureDto.picture())) {
            throw new IllegalArgumentException("Invalid file type for picture");
        }

        // Map DTO to Feature entity
        Feature feature = featureMapper.toFeatureScheme(createUpdateFeatureDto);

        // Upload the new picture
        String pictureKey = fileStorageService.uploadFile(createUpdateFeatureDto.picture(), "features").join();
        feature = feature.withPicture(pictureKey);

        // Save to MongoDB
        Feature savedFeature = mongoTemplate.save(feature);
        return getFeatureWithPictureUrl(savedFeature);
    }

    /**
     * Finds a feature by its id.
     *
     * @param id the id of the feature to find
     * @return the feature
     */
    public FeatureDto findFeatureById(String id) {
        Feature feature = mongoTemplate.findById(id, Feature.class);
        if (feature == null) {
            return null;
        }
        return getFeatureWithPictureUrl(feature);
    }

    /**
     * Finds all features.
     *
     * @return a list of features
     */
    public List<FeatureDto> findAllFeatures() {
        List<Feature> features = mongoTemplate.findAll(Feature.class);
        return features.stream()
                .map(this::getFeatureWithPictureUrl)
                .toList();
    }

    /**
     * Updates a feature by its id.
     *
     * @param id                   the id of the feature to update
     * @param createUpdateFeatureDto the updated feature
     * @return the updated feature
     */
    public FeatureDto updateFeature(String id, CreateUpdateFeatureDto createUpdateFeatureDto) {
        // Validate picture file
        if (createUpdateFeatureDto.picture() != null && isImageFileInvalid(createUpdateFeatureDto.picture())) {
            throw new IllegalArgumentException("Invalid file type for picture");
        }

        // Check if a Feature document already exists
        Feature existingFeature = mongoTemplate.findById(id, Feature.class);
        if (existingFeature == null) {
            throw new IllegalArgumentException("Feature document not found");
        }

        // Map DTO to Feature entity and retain the existing ID
        Feature feature = featureMapper.toFeatureScheme(createUpdateFeatureDto).withId(id);

        // Delete the old picture if a new one is provided
        if (createUpdateFeatureDto.picture() != null && existingFeature.picture() != null) {
            fileStorageService.deleteFile(existingFeature.picture()).join();
        }

        // Upload the new picture if provided
        if (createUpdateFeatureDto.picture() != null) {
            String pictureKey = fileStorageService.uploadFile(createUpdateFeatureDto.picture(), "features").join();
            feature = feature.withPicture(pictureKey);
        }

        // Save to MongoDB
        Feature updatedFeature = mongoTemplate.save(feature);
        return getFeatureWithPictureUrl(updatedFeature);
    }

    /**
     * Deletes a feature by its id.
     *
     * @param id the id of the feature to delete
     */
    public void deleteFeature(String id) {
        Feature feature = mongoTemplate.findById(id, Feature.class);
        if (feature != null) {
            // Delete the picture if it exists
            if (feature.picture() != null) {
                fileStorageService.deleteFile(feature.picture()).join();
            }
            mongoTemplate.remove(feature);
        } else {
            throw new IllegalArgumentException("Feature document not found");
        }
    }
}
