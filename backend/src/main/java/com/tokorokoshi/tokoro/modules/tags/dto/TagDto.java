package com.tokorokoshi.tokoro.modules.tags.dto;

import jakarta.annotation.Nonnull;

public class TagDto {
    @Nonnull
    private String lang;

    @Nonnull
    private String name;

    public TagDto() {
        this.lang = "en";
        this.name = "";
    }

    public TagDto(String lang, String name) {
        this.lang = lang;
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Tag{lang=" + lang + ", value=" + name + "}";
    }
}
