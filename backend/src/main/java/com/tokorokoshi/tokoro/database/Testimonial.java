package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a testimonial in the database.
 */
@Document(collection = "testimonial")
public record Testimonial(
        @Id
        @NotNull(message = "ID cannot be null")
        String id,

        @NotNull(message = "User ID cannot be null")
        String userId,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        int rating,

        @NotNull(message = "Message cannot be null")
        @NotBlank(message = "Message cannot be blank")
        @Length(max = 300, message = "Message must be at most 300 characters")
        String message,

        @CreatedDate
        LocalDate createdAt,

        @NotNull(message = "Status cannot be null")
        Status status
) {
    /**
     * Compact constructor to enforce default status as PENDING.
     */
    public Testimonial {
        if (status == null) {
            status = Status.PENDING;
        }
    }

    /**
     * Creates a new testimonial with the specified ID.
     *
     * @param id The ID of the testimonial
     * @return A new testimonial with the specified ID
     */
    public Testimonial withId(String id) {
        return new Testimonial(
                id,
                userId,
                rating,
                message,
                createdAt,
                status
        );
    }

    /**
     * Creates a new testimonial with the specified user ID.
     *
     * @param userId The user ID associated with this testimonial
     * @return A new testimonial with the specified user ID
     */
    public Testimonial withUserId(String userId) {
        return new Testimonial(
                id,
                userId,
                rating,
                message,
                createdAt,
                status
        );
    }

    /**
     * Creates a new testimonial with the specified status.
     *
     * @param status The new status for the testimonial
     * @return A new testimonial with the updated status
     */
    public Testimonial withStatus(Status status) {
        return new Testimonial(
                id,
                userId,
                rating,
                message,
                createdAt,
                status
        );
    }

    /**
     * Enum representing the possible statuses of a testimonial.
     */
    public enum Status {
        /**
         * The testimonial is pending approval.
         */
        PENDING,

        /**
         * The testimonial has been approved.
         */
        APPROVED,

        /**
         * The testimonial has been rejected.
         */
        REJECTED
    }
}