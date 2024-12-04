package com.tokorokoshi.tokoro.modules.user.dto;

import java.util.List;

public class UserPreferencesDto {
    private String language;
    private List<String> tags;
    private List<String> categories;

    public UserPreferencesDto(){
        this.language = "";
        this.tags = List.of();
        this.categories = List.of();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
