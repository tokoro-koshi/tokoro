package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class UserPreferences {
    @NonNull
    private String language;

    @NonNull
    @DBRef
    private List<ObjectId> tags;

    @NonNull
    @DBRef
    private List<String> categories;

    public UserPreferences() {
        this.language = "";
        this.tags = List.of();          // empty list
        this.categories = List.of();    // empty list
    }


    @NonNull
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull String language) {
        this.language = language;
    }

    @NonNull
    public List<ObjectId> getTags() {
        return tags;
    }

    public void setTags(@NonNull List<ObjectId> tags) {
        this.tags = tags;
    }

    @NonNull
    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(@NonNull List<String> categories) {
        this.categories = categories;
    }
}