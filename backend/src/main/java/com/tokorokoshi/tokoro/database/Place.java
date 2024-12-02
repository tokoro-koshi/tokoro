package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "place")
public class Place {
    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String name;

    private String description;

    @NonNull
    @DBRef
    private Location location;

    private String categoryId;

    @DBRef
    @NonNull
    private List<HashTag> hashTags;

    @NonNull
    private double rating;

    public Place() {
        this.name = "";
        this.description = "";
        this.location = new Location();
        this.hashTags = List.of();
        this.rating = 0;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(@NonNull List<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    @NonNull
    public double getRating() {
        return rating;
    }

    public void setRating(@NonNull double rating) {
        this.rating = rating;
    }
}
