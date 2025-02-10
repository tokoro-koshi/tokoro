package com.tokorokoshi.tokoro.modules.about;

import com.tokorokoshi.tokoro.database.About;
import com.tokorokoshi.tokoro.modules.about.dto.AboutDto;
import com.tokorokoshi.tokoro.modules.about.dto.CreateUpdateAboutDto;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class AboutService {

    private final MongoTemplate mongoTemplate;
    private final AboutMapper aboutMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public AboutService(
            MongoTemplate mongoTemplate,
            AboutMapper aboutMapper,
            FileStorageService fileStorageService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.aboutMapper = aboutMapper;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Get about with logo URL.
     */
    private AboutDto getAboutWithLogoUrl(About about) {
        String logoUrl = fileStorageService.generateSignedUrl(about.logo()).join();
        return aboutMapper.toAboutDto(about.withLogo(logoUrl));
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
     * Retrieves the single About document.
     *
     * @return the About document
     */
    public AboutDto getAbout() {
        Optional<About> aboutOptional = Optional.ofNullable(mongoTemplate.findOne(new Query(), About.class));
        return aboutOptional.map(this::getAboutWithLogoUrl).orElse(null);
    }

    /**
     * Creates a new About document.
     *
     * @param createUpdateAboutDto the data to create
     * @return the created About document
     */
    public AboutDto createAbout(CreateUpdateAboutDto createUpdateAboutDto) {
        // Validate logo file
        if (isImageFileInvalid(createUpdateAboutDto.logo())) {
            throw new IllegalArgumentException("Invalid file type for logo");
        }

        // Map DTO to About entity
        About about = aboutMapper.toAbout(createUpdateAboutDto);

        // Upload the new logo
        String appLogoKey = fileStorageService.uploadFile(createUpdateAboutDto.logo(), "about").join();
        about = about.withLogo(appLogoKey);

        // Save to MongoDB
        About savedAbout = mongoTemplate.save(about);
        return getAboutWithLogoUrl(savedAbout);
    }

    /**
     * Updates an existing About document.
     *
     * @param createUpdateAboutDto the data to update
     * @return the updated About document
     */
    public AboutDto updateAbout(CreateUpdateAboutDto createUpdateAboutDto) {
        // Validate logo file
        if (createUpdateAboutDto.logo() != null && isImageFileInvalid(createUpdateAboutDto.logo())) {
            throw new IllegalArgumentException("Invalid file type for logo");
        }

        // Check if an About document already exists
        About existingAbout = mongoTemplate.findOne(new Query(), About.class);
        if (existingAbout == null) {
            throw new IllegalArgumentException("About document not found");
        }

        // Map DTO to About entity and retain the existing ID
        About about = aboutMapper.toAbout(createUpdateAboutDto).withId(existingAbout.id());

        // Delete the old logo if a new one is provided
        if (createUpdateAboutDto.logo() != null) {
            fileStorageService.deleteFile(existingAbout.logo()).join();
        }

        // Upload the new logo if provided
        if (createUpdateAboutDto.logo() != null) {
            String appLogoKey = fileStorageService.uploadFile(createUpdateAboutDto.logo(), "about").join();
            about = about.withLogo(appLogoKey);
        }

        // Save to MongoDB
        About savedAbout = mongoTemplate.save(about);
        return getAboutWithLogoUrl(savedAbout);
    }

    /**
     * Deletes the About document.
     */
    public void deleteAbout() {
        Optional<About> aboutOptional = Optional.ofNullable(mongoTemplate.findOne(new Query(), About.class));
        if (aboutOptional.isPresent()) {
            About about = aboutOptional.get();
            // Delete the logo if it exists
            if (about.logo() != null) {
                fileStorageService.deleteFile(about.logo()).join();
            }
            mongoTemplate.remove(about);
        } else {
            throw new IllegalArgumentException("About document not found");
        }
    }
}