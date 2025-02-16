package com.tokorokoshi.tokoro.modules.testimonials;

import com.tokorokoshi.tokoro.database.Testimonial;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.testimonials.dto.CreateUpdateTestimonialDto;
import com.tokorokoshi.tokoro.modules.testimonials.dto.TestimonialDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TestimonialsService {

    private final MongoTemplate mongoTemplate;
    private final TestimonialMapper testimonialMapper;
    private final Auth0UserDataService auth0UserDataService;

    @Autowired
    public TestimonialsService(
            MongoTemplate mongoTemplate,
            TestimonialMapper testimonialMapper,
            Auth0UserDataService auth0UserDataService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.testimonialMapper = testimonialMapper;
        this.auth0UserDataService = auth0UserDataService;
    }

    /**
     * Saves a new testimonial.
     *
     * @param createUpdateTestimonialDto testimonial data
     * @return the saved testimonial
     */
    public TestimonialDto saveTestimonial(CreateUpdateTestimonialDto createUpdateTestimonialDto) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Testimonial testimonial = testimonialMapper.toTestimonialSchema(createUpdateTestimonialDto);
        testimonial = testimonial.withUserId(userId);
        Testimonial savedTestimonial = mongoTemplate.save(testimonial);

        return testimonialMapper.toTestimonialDto(savedTestimonial);
    }

    /**
     * Retrieves a testimonial by ID.
     *
     * @param id testimonial ID
     * @return the testimonial
     */
    public TestimonialDto getTestimonialById(String id) {
        Testimonial testimonial = mongoTemplate.findById(id, Testimonial.class);
        if (testimonial == null) return null;

        return testimonialMapper.toTestimonialDto(testimonial);
    }

    /**
     * Retrieves paginated testimonials.
     *
     * @param pageable pagination information (page, size)
     * @return paginated list of testimonials
     */
    public Page<TestimonialDto> getAllTestimonials(Pageable pageable) {
        // Create a query with pagination information
        Query query = Query.query(new Criteria()).with(pageable);
        List<Testimonial> testimonials = mongoTemplate.find(query, Testimonial.class);

        // Count the total number of testimonials
        long total = mongoTemplate.count(new Query(), Testimonial.class);

        List<TestimonialDto> content = testimonials.stream()
                .map(testimonialMapper::toTestimonialDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Deletes a testimonial by ID.
     *
     * @param id testimonial ID
     */
    public void deleteTestimonial(String id) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Testimonial testimonial = mongoTemplate.findById(id, Testimonial.class);
        if (testimonial == null) {
            throw new IllegalArgumentException("Testimonial not found for id: " + id);
        }

        // Check if the user is authorized to delete the testimonial
        if (!Objects.equals(testimonial.userId(), userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this testimonial");
        }

        mongoTemplate.remove(testimonial);
    }

    /**
     * Retrieves testimonials for a specific user.
     *
     * @param userId   user ID
     * @param pageable pagination information (page, size)
     * @return paginated list of testimonials for the user
     */
    public Page<TestimonialDto> getUserTestimonials(String userId, Pageable pageable) {
        // Create a query with pagination information and filter by user ID
        Query query = Query.query(Criteria.where("userId").is(userId)).with(pageable);

        List<Testimonial> testimonials = mongoTemplate.find(query, Testimonial.class);

        // Count the total number of testimonials for the user
        long total = mongoTemplate.count(Query.query(Criteria.where("userId").is(userId)), Testimonial.class);

        List<TestimonialDto> content = testimonials.stream()
                .map(testimonialMapper::toTestimonialDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Updates an existing testimonial and sets its status to PENDING.
     *
     * @param id                         testimonial ID
     * @param createUpdateTestimonialDto testimonial data to update
     * @return the updated testimonial
     */
    public TestimonialDto updateTestimonial(String id, CreateUpdateTestimonialDto createUpdateTestimonialDto) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Testimonial existingTestimonial = mongoTemplate.findById(id, Testimonial.class);
        if (existingTestimonial == null) {
            throw new IllegalArgumentException("Testimonial not found for id: " + id);
        }

        // Check if the user is authorized to update the testimonial
        if (!Objects.equals(existingTestimonial.userId(), userId)) {
            throw new IllegalArgumentException("User is not authorized to update this testimonial");
        }

        Testimonial testimonial = testimonialMapper.toTestimonialSchema(createUpdateTestimonialDto).withId(existingTestimonial.id());

        // Set the new status to PENDING
        testimonial = testimonial.withStatus(Testimonial.Status.PENDING);
        Testimonial savedTestimonial = mongoTemplate.save(testimonial);

        return testimonialMapper.toTestimonialDto(savedTestimonial);
    }

    /**
     * Changes the status of a testimonial.
     *
     * @param id     testimonial ID
     * @param status new status for the testimonial
     */
    public void changeTestimonialStatus(String id, Testimonial.Status status) {
        Testimonial existingTestimonial = mongoTemplate.findById(id, Testimonial.class);
        if (existingTestimonial == null) {
            throw new IllegalArgumentException("Testimonial not found for id: " + id);
        }
        Testimonial testimonial = existingTestimonial.withStatus(status);

        mongoTemplate.save(testimonial);
    }

    /**
     * Retrieves paginated testimonials by status.
     *
     * @param status   testimonial status
     * @param pageable pagination information (page, size)
     * @return paginated list of testimonials with the given status
     */
    public Page<TestimonialDto> getTestimonialsByStatus(Testimonial.Status status, Pageable pageable) {
        // Create a query with pagination information and filter by status
        Query query = Query.query(Criteria.where("status").is(status)).with(pageable);

        List<Testimonial> testimonials = mongoTemplate.find(query, Testimonial.class);

        // Count the total number of testimonials with the given status
        long total = mongoTemplate.count(Query.query(Criteria.where("status").is(status)), Testimonial.class);

        List<TestimonialDto> content = testimonials.stream()
                .map(testimonialMapper::toTestimonialDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Retrieves random testimonials with APPROVED status.
     *
     * @param count number of random testimonials to retrieve
     * @return list of random testimonials
     */
    public List<TestimonialDto> getRandomApprovedTestimonials(int count) {
        // Define an aggregation pipeline to match testimonials with APPROVED status and sample a specified number
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is(Testimonial.Status.APPROVED)),
                Aggregation.sample(count)
        );

        AggregationResults<Testimonial> results = mongoTemplate.aggregate(aggregation, Testimonial.class, Testimonial.class);

        return results.getMappedResults().stream()
                .map(testimonialMapper::toTestimonialDto)
                .collect(Collectors.toList());
    }
}